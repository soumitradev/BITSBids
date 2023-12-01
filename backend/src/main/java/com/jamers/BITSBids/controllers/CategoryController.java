package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.Category;
import com.jamers.BITSBids.models.Product;
import com.jamers.BITSBids.models.User;
import com.jamers.BITSBids.repositories.CategoryRepository;
import com.jamers.BITSBids.repositories.ProductRepository;
import com.jamers.BITSBids.repositories.UserRepository;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.AuthUserError;
import com.jamers.BITSBids.response_types.errors.CategoryFetchError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class CategoryController {
	final DatabaseClient client;
	final UserRepository userRepository;
	final CategoryRepository categoryRepository;
	final ProductRepository productRepository;


	public CategoryController(DatabaseClient client, UserRepository userRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.client = client;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@GetMapping("/categories")
	public ResponseEntity<GenericResponseType> getCategories(
					@AuthenticationPrincipal
					OAuth2User principal) {
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

		final List<Category> categories = categoryRepository.listCategories().collectList().block();
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						categories,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}


	@GetMapping("/categories/{id}")
	public ResponseEntity<GenericResponseType> getProductsByCategory(
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

		final Category currentCategory = categoryRepository.findById(String.valueOf(id)).block();

		if (currentCategory == null) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											CategoryFetchError.invalidCategoryError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
			);
		}

		final List<Product> products = categoryRepository.listProductByCategory(id).collectList().block();
		return new ResponseEntity<GenericResponseType>(new GenericResponseType(
						products,
						GenericResponseType.ResponseStatus.SUCCESS
		), HttpStatus.OK);
	}

}