package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.*;
import com.jamers.BITSBids.repositories.*;
import com.jamers.BITSBids.request_models.BidCreateData;
import com.jamers.BITSBids.request_models.ProductCreateData;
import com.jamers.BITSBids.response_models.CategorizedProduct;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.jamers.BITSBids.common.Constants.MIN_BID_DELTA;


@RestController
public class ProductController {
	final DatabaseClient client;
	final ProductRepository productRepository;
	final UserRepository userRepository;
	final BidRepository bidRepository;
	final CategoryRepository categoryRepository;
	final ProductCategoryRepository productCategoryRepository;

	public ProductController(DatabaseClient client, ProductRepository productRepository, UserRepository userRepository, BidRepository bidRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository) {
		this.client = client;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.bidRepository = bidRepository;
		this.categoryRepository = categoryRepository;
		this.productCategoryRepository = productCategoryRepository;

	}

	@PostMapping(
					path = "/product/create",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<GenericResponseType> create(
					@AuthenticationPrincipal
					OAuth2User principal,
					@RequestBody
					@Validated
					ProductCreateData productCreateData) {
		if (productCreateData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											ProductCreateError.nullProductError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		Product product = new Product(
						null,
						productCreateData.name(),
						productCreateData.description(),
						currentUser.id(),
						productCreateData.media(),
						productCreateData.basePrice(),
						productCreateData.autoSellPrice(),
						productCreateData.basePrice(),
						false,
						null,
						productCreateData.closedAt(),
						null
		);
		Product currentProduct = productRepository.save(product).block();
		for (String categoryName : productCreateData.category()) {
			Category category = categoryRepository.getCategoryByName(categoryName).blockFirst();
			if (category == null) {
				return new ResponseEntity<GenericResponseType>(
								new GenericResponseType(
												ProductCreateError.invalidCategoryError(),
												GenericResponseType.ResponseStatus.ERROR
								),
								HttpStatus.BAD_REQUEST
				);
			}
		}
		for (String categoryName : productCreateData.category()) {
			Category category = categoryRepository.getCategoryByName(categoryName).blockFirst();
			ProductCategory productCategory = new ProductCategory(null, currentProduct.id(), category.id());
			productCategoryRepository.save(productCategory).block();
		}
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						currentProduct,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.CREATED);
	}

	@PostMapping(
					path = "/product/{id}/bid",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<GenericResponseType> createBid(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id,
					@Validated
					@RequestBody
					BidCreateData bidCreateData) {

		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();
		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											ProductFetchError.invalidProductError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		if (bidCreateData == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							BidCreateError.nullBidError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		Bid bid = new Bid(null, currentProduct.id(), currentUser.id(), bidCreateData.price(), null);


		if (bidCreateData.price() < (1 + MIN_BID_DELTA / 100.0) * currentProduct.price()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							BidCreateError.invalidBidError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.NOT_ACCEPTABLE);
		}

		Bid currentBid = bidRepository.save(bid).block();

		if (currentBid == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											BidCreateError.internalServerError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.INTERNAL_SERVER_ERROR
			);
		}

		Product newProduct = new Product(
						currentProduct.id(),
						currentProduct.name(),
						currentProduct.description(),
						currentProduct.sellerId(),
						currentProduct.media(),
						currentProduct.basePrice(),
						currentProduct.autoSellPrice(),
						currentBid.price(),
						currentProduct.sold(),
						currentProduct.createdAt(),
						currentProduct.closedAt(),
						currentBid.id()
		);
		productRepository.save(newProduct).block();
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						currentBid,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.ACCEPTED);

	}

	@GetMapping("/product/{id}")
	public ResponseEntity<GenericResponseType> getProduct(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											ProductFetchError.invalidProductError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		} else {
			List<Bid> bids = bidRepository.getLastTenBids(id).collectList().block();
			List<String> categories = categoryRepository.listProductCategories(id).collectList().block();
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											new CategorizedProduct(currentProduct, categories, bids),
											GenericResponseType.ResponseStatus.SUCCESS
							),
							HttpStatus.ACCEPTED
			);
		}
	}

	@PostMapping("/product/{id}/delete")
	public ResponseEntity<GenericResponseType> deleteProduct(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id) {

		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											ProductFetchError.invalidProductError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		if (currentUser.id() == currentProduct.sellerId()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											productRepository.deleteById(String.valueOf(id)).block(),
											GenericResponseType.ResponseStatus.SUCCESS
							),
							HttpStatus.ACCEPTED
			);
		} else {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											ProductDeleteError.notProductSellerError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
	}
}
