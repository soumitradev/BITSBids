package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.Bid;
import com.jamers.BITSBids.models.Conversation;
import com.jamers.BITSBids.models.Product;
import com.jamers.BITSBids.models.User;
import com.jamers.BITSBids.repositories.BidRepository;
import com.jamers.BITSBids.repositories.ConversationRepository;
import com.jamers.BITSBids.repositories.ProductRepository;
import com.jamers.BITSBids.repositories.UserRepository;
import com.jamers.BITSBids.request_models.UserCreateData;
import com.jamers.BITSBids.request_models.UserEditData;
import com.jamers.BITSBids.response_models.ProductBidPair;
import com.jamers.BITSBids.response_models.UserDetails;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.AuthUserError;
import com.jamers.BITSBids.response_types.errors.BidFetchError;
import com.jamers.BITSBids.response_types.errors.UserCreateError;
import com.jamers.BITSBids.response_types.errors.UserEditError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jamers.BITSBids.common.Constants.INITIAL_BALANCE;

@RestController
public class UserController {
	final DatabaseClient client;
	final UserRepository userRepository;
	final ConversationRepository conversationRepository;
	final ProductRepository productRepository;
	final BidRepository bidRepository;

	public UserController(DatabaseClient client, UserRepository userRepository, ConversationRepository conversationRepository, ProductRepository productRepository, BidRepository bidRepository) {
		this.client = client;
		this.userRepository = userRepository;
		this.conversationRepository = conversationRepository;
		this.productRepository = productRepository;
		this.bidRepository = bidRepository;
	}

	@PostMapping(
					path = "/user/create",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<GenericResponseType> create(
					@AuthenticationPrincipal
					OAuth2User principal,
					@RequestBody
					@Validated
					UserCreateData userCreateData) {
		if (userCreateData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.nullUserError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
		if (principal.getAttribute("email") == null || Objects.requireNonNull(principal.getAttribute("email")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("email")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.nullEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}

		if (principal.getAttribute("name") == null || Objects.requireNonNull(principal.getAttribute("name")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("name")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.nullNameError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}

		if (userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst() != null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.userAlreadyExistsError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		Pattern pattern = Pattern.compile(
						"f(\\d{4})\\d{4}@(hyderabad|goa|pilani).bits-pilani.ac.in",
						Pattern.CASE_INSENSITIVE
		);
		Matcher matcher = pattern.matcher(Objects.requireNonNull(principal.getAttribute("email")));
		if (matcher.find()) {
			int batch;
			try {
				batch = Integer.parseInt(matcher.group(1));
			} catch (NumberFormatException e) {
				return new ResponseEntity<GenericResponseType>(
								new GenericResponseType(
												UserCreateError.invalidEmailError(),
												GenericResponseType.ResponseStatus.ERROR
								),
								HttpStatus.UNAUTHORIZED
				);
			}
			User user = new User(
							null,
							Objects.requireNonNull(principal.getAttribute("email")).toString(),
							Objects.requireNonNull(principal.getAttribute("name")).toString(),
							batch,
							userCreateData.room(),
							userCreateData.phoneNumber(),
							INITIAL_BALANCE
			);
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							userRepository.save(user).block(),
							GenericResponseType.ResponseStatus.SUCCESS
			), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.invalidEmailError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.UNAUTHORIZED
			);
		}
	}

	@GetMapping("/user/me")
	public ResponseEntity<GenericResponseType> getMe(
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
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();

		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.userNotFoundError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						currentUser,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@PostMapping(
					path = "/user/delete"
	)
	public ResponseEntity<GenericResponseType> delete(
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
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();
		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											userRepository.delete(currentUser).block(),
											GenericResponseType.ResponseStatus.SUCCESS
							),
							HttpStatus.OK
			);
		}
	}

	@PostMapping(
					path = "/user/edit",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<GenericResponseType> edit(
					@AuthenticationPrincipal
					OAuth2User principal,
					@RequestBody
					@Validated
					UserEditData userEditData) {
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
		if (userEditData == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserEditError.nullFieldsError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		String newRoom = currentUser.room();
		BigInteger newPhoneNumber = currentUser.phoneNumber();

		if (userEditData.room() != null) {
			newRoom = userEditData.room();
		}
		if (userEditData.phoneNumber() != null) {
			newPhoneNumber = userEditData.phoneNumber();
		}

		User user = new User(
						currentUser.id(),
						currentUser.email(),
						currentUser.name(),
						currentUser.batch(),
						newRoom,
						newPhoneNumber,
						currentUser.balance()
		);

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						userRepository.save(user).block(),
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@GetMapping("/user/products")
	public ResponseEntity<GenericResponseType> getUserProducts(
					@AuthenticationPrincipal
					OAuth2User principal,
					@RequestParam(
									name = "active",
									required = true
					)
					boolean active) {
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
			), HttpStatus.BAD_REQUEST);
		}
		int userId = currentUser.id();

		List<Product> products;
		if (active) {
			products = productRepository.findActiveProductsById(userId).collectList().block();
		} else {
			products = productRepository.findSoldProductsById(userId).collectList().block();
		}
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						products,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@GetMapping("/user/bids")
	public ResponseEntity<GenericResponseType> getUserBids(
					@AuthenticationPrincipal
					OAuth2User principal,
					@RequestParam(
									name = "active",
									required = true
					)
					boolean active) {
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
		int userId = currentUser.id();

		List<ProductBidPair> productBidPairs;
		List<Bid> bids;
		List<Product> products;
		if (active) {
			bids = bidRepository.findActiveBidsByUserId(userId).collectList().block();
			products = bidRepository.findActiveBidProductsByUserId(userId).collectList().block();
		} else {
			bids = bidRepository.findInactiveBidsByUserId(userId).collectList().block();
			products = bidRepository.findInactiveBidProductsByUserId(userId).collectList().block();
		}
		if (bids == null && products == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							new ArrayList<>(),
							GenericResponseType.ResponseStatus.SUCCESS
			), HttpStatus.OK);
		}
		if (products == null || bids == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							BidFetchError.invalidBidError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		productBidPairs = ProductBidPair.zip(products, bids);
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						productBidPairs,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@GetMapping("/user/conversations")
	public ResponseEntity<GenericResponseType> conversations(
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
		final User currentUser = userRepository.findByEmail(Objects.requireNonNull(principal.getAttribute("email")).toString()).blockFirst();
		if (currentUser == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.nullUserError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.UNAUTHORIZED);
		}

		final List<Conversation> conversationList =
						conversationRepository.findUserConversations(currentUser.id()).collectList().block();
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						conversationList,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<GenericResponseType> getUserDetails(
					@AuthenticationPrincipal
					OAuth2User principal,
					@PathVariable
					int userId) {
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

		
		final User user = userRepository.findById(String.valueOf(userId)).block();
		if (user == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.userNotFoundError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.NOT_FOUND);
		}

		Product product1 = productRepository.findSoldProduct(currentUser.id(), userId).blockFirst();
		Product product2 = productRepository.findSoldProduct(userId, currentUser.id()).blockFirst();

		if (product1 == null && product2 == null) {
			return new ResponseEntity<GenericResponseType>(new GenericResponseType(
							AuthUserError.userAnonymityError(),
							GenericResponseType.ResponseStatus.ERROR
			), HttpStatus.FORBIDDEN);
		}

		UserDetails userDetails = new UserDetails(
						user.name(),
						user.email(),
						user.phoneNumber(),
						user.room(),
						user.batch()
		);

		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						userDetails,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}
}
