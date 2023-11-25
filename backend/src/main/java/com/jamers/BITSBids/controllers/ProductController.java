package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.Product;
import com.jamers.BITSBids.models.User;
import com.jamers.BITSBids.repositories.ProductRepository;
import com.jamers.BITSBids.repositories.UserRepository;
import com.jamers.BITSBids.request_models.ProductCreateData;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.AuthUserError;
import com.jamers.BITSBids.response_types.errors.ProductCreateError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
public class ProductController {
	final DatabaseClient client;
	final ProductRepository productRepository;
	final UserRepository userRepository;

	public ProductController(DatabaseClient client, ProductRepository productRepository, UserRepository userRepository) {
		this.client = client;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
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
}
