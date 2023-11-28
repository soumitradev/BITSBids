package com.jamers.BITSBids.controllers;

import com.jamers.BITSBids.models.User;
import com.jamers.BITSBids.repositories.UserRepository;
import com.jamers.BITSBids.request_models.UserCreateData;
import com.jamers.BITSBids.response_types.GenericResponseType;
import com.jamers.BITSBids.response_types.errors.UserCreateError;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jamers.BITSBids.common.Constants.INITIAL_BALANCE;

@RestController
public class UserController {
	final DatabaseClient client;
	final UserRepository userRepository;

	public UserController(DatabaseClient client, UserRepository userRepository) {
		this.client = client;
		this.userRepository = userRepository;
	}

	@PostMapping(
					path = "/api/user/create",
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

		if (principal.getAttribute("name") == null || Objects.requireNonNull(principal.getAttribute("name")).toString().isEmpty() || Objects.requireNonNull(
						principal.getAttribute("name")).toString().isBlank()) {
			return new ResponseEntity<GenericResponseType>(
							new GenericResponseType(
											UserCreateError.nullNameError(),
											GenericResponseType.ResponseStatus.ERROR
							),
							HttpStatus.BAD_REQUEST
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
			int batch = 0;
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
}