package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.*;
import com.jamers.BITSBids.repositories.*;
import com.jamers.BITSBids.request_models.BidCreateData;
import com.jamers.BITSBids.request_models.MessageCreateData;
import com.jamers.BITSBids.request_models.MessageReadData;
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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	final ConversationRepository conversationRepository;
	final MessageRepository messageRepository;
	final Map<String, HashSet<WebSocketSession>> sessions;

	public ProductController(DatabaseClient client, ProductRepository productRepository, UserRepository userRepository, BidRepository bidRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ConversationRepository conversationRepository, MessageRepository messageRepository, Map<String, HashSet<WebSocketSession>> sessions) {
		this.client = client;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.bidRepository = bidRepository;
		this.categoryRepository = categoryRepository;
		this.productCategoryRepository = productCategoryRepository;
		this.conversationRepository = conversationRepository;
		this.messageRepository = messageRepository;
		this.sessions = sessions;
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
							HttpStatus.UNAUTHORIZED
			);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
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
							AuthUserError.nullEmailError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();
		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
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
		), HttpStatus.OK);

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
							AuthUserError.nullEmailError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
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
			List<Category> categories = categoryRepository.listProductCategories(id).collectList().block();
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											new CategorizedProduct(currentProduct, categories, bids),
											GenericResponseType.ResponseStatus.SUCCESS
							),
							HttpStatus.OK
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
							AuthUserError.nullEmailError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
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
							HttpStatus.OK
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

	@GetMapping("/product/latest")
	public ResponseEntity<GenericResponseType> getLatestProducts(
					@AuthenticationPrincipal
					OAuth2User principal) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);

		}
		final List<Product> latestProducts = productRepository.findLatestProducts().collectList().block();
		return new ResponseEntity<GenericResponseType>(
						new GenericResponseType(
										latestProducts,
										GenericResponseType.ResponseStatus.SUCCESS
						),
						HttpStatus.OK
		);
	}

	@GetMapping("/product/{id}/messages")
	public ResponseEntity<GenericResponseType> getProductMessages(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		Conversation conversation = conversationRepository.findByProductId(currentUser.id(), id).blockFirst();
		if (conversation == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ConversationFetchError.invalidConversationError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		List<Message> messages = messageRepository.findByConversationId(conversation.id()).collectList().block();

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						messages,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@PostMapping("/product/{id}/send")
	public ResponseEntity<GenericResponseType> sendProductMessage(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id,
					@Validated
					@RequestBody
					MessageCreateData messageCreateData) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		Conversation conversation = conversationRepository.findByProductId(currentUser.id(), id).blockFirst();

		if (conversation == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ConversationFetchError.invalidConversationError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		if (messageCreateData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											MessageCreateError.nullMessageError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		if ((messageCreateData.text() == null || messageCreateData.text().isEmpty() || messageCreateData.text().isBlank()) &&
						(messageCreateData.media() == null || messageCreateData.media().isEmpty())) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											MessageCreateError.emptyMessageError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		Message message = new Message(
						null,
						conversation.id(),
						currentUser.id() == conversation.buyerId(),
						messageCreateData.text(),
						messageCreateData.media(),
						null
		);

		User otherUser = userRepository.findById(String.valueOf(currentUser.id() == conversation.buyerId() ? conversation.sellerId() : conversation.buyerId())).block();
		HashSet<WebSocketSession> otherSessions = sessions.get(otherUser.email());
		if (otherSessions != null) {
			try {
				for (WebSocketSession otherSession : otherSessions) {
					otherSession.sendMessage(new TextMessage(String.valueOf(conversation.id())));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<GenericResponseType>(new GenericResponseType(
								MessageCreateError.internalServerError(),
								GenericResponseType.ResponseStatus.ERROR
				), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						messageRepository.save(message).block(),
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@PostMapping("/product/{id}/readMessages")
	public ResponseEntity<GenericResponseType> sendProductMessage(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int id,
					@Validated
					@RequestBody
					MessageReadData messageReadData) {
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											AuthUserError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final Product currentProduct = productRepository.findById(String.valueOf(id)).block();

		if (currentProduct == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ProductFetchError.invalidProductError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		Conversation conversation = conversationRepository.findByProductId(currentUser.id(), id).blockFirst();

		if (conversation == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							ConversationFetchError.invalidConversationError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.BAD_REQUEST);
		}

		if (messageReadData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											MessageReadError.nullMessageError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		Message message = messageRepository.findById(String.valueOf(messageReadData.messageId())).block();

		if (message == null || message.conversationId() != conversation.id()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											MessageReadError.invalidMessageError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		Conversation newConversation;
		if (currentUser.id() == conversation.buyerId()) {
			newConversation = new Conversation(
							conversation.id(),
							conversation.sellerId(),
							conversation.buyerId(),
							conversation.productId(),
							conversation.lastReadBySellerId(),
							messageReadData.messageId()
			);
		} else {
			newConversation = new Conversation(
							conversation.id(),
							conversation.sellerId(),
							conversation.buyerId(),
							conversation.productId(),
							messageReadData.messageId(),
							conversation.lastReadByBuyerId()
			);
		}

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						conversationRepository.save(newConversation).block(),
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}
}

