package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Product;
import com.jamers.BITSBids.models.User;
import com.jamers.BITSBids.repositories.BidRepository;
import com.jamers.BITSBids.repositories.ProductRepository;
import com.jamers.BITSBids.repositories.UserRepository;
import com.jamers.BITSBids.request_models.BidCreateData;
import com.jamers.BITSBids.request_models.ProductCreateData;
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
import java.util.ArrayList;

import static com.jamers.BITSBids.common.Constants.MIN_BID_DELTA;


@RestController
public class ProductController {
	final DatabaseClient client;
	final ProductRepository productRepository;
	final UserRepository userRepository;
	final BidRepository bidRepository;

	public ProductController(DatabaseClient client, ProductRepository productRepository, UserRepository userRepository,
	                         BidRepository bidRepository) {
		this.client = client;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.bidRepository = bidRepository;

	}

	@PostMapping(
					path = "/api/product/create",
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
											UserCreateError.nullEmailError(),
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
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						productRepository.save(product).block(),
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.CREATED);
	}

	@PostMapping(
					path = "/api/product/{id}/bid",
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
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullUserError(),
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

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		if (bidCreateData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											BidCreateError.nullBidError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		Bid bid = new Bid(
						null,
						currentProduct.id(),
						currentUser.id(),
						bidCreateData.price(),
						null
		);


		if (bidCreateData.price() < (1 + MIN_BID_DELTA / 100.0) * currentProduct.price()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							BidCreateError.invalidBidError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.NOT_ACCEPTABLE);
		}

		Bid currentBid = bidRepository.save(bid).block();
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

	@GetMapping("/api/product/{id}")
	public ResponseEntity<GenericResponseType> getProduct(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullUserError(),
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

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							currentProduct,
							GenericResponseType.ResponseStatus.SUCCESS
			), HttpStatus.ACCEPTED);
		}
	}

	@PostMapping("/api/product/{id}/delete")
	public ResponseEntity<GenericResponseType> deleteProduct(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id) {

		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullUserError(),
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

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		if (currentUser.id() == currentProduct.sellerId()) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							productRepository.deleteById(String.valueOf(id)).block(),
							GenericResponseType.ResponseStatus.SUCCESS
			), HttpStatus.ACCEPTED);
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

	@GetMapping("/api/product/latest")
	public ResponseEntity<GenericResponseType> getLatestProducts(
					@AuthenticationPrincipal
					OAuth2User principal) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullUserError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);

		}
		ArrayList<Product> latestProducts = productRepository.findLatestProducts().blockFirst();
		return new ResponseEntity<GenericResponseType>(
						new GenericResponseType(
										latestProducts,
										GenericResponseType.ResponseStatus.SUCCESS
						),
						HttpStatus.ACCEPTED
		);
	}

}
