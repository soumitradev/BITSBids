package com.jamers.BITSBids.response_types.errors;

public class AuthUserError extends GenericError {
	public static AuthUserError nullUserError() {
		AuthUserError error = new AuthUserError();
		error.error = "NullUserError";
		error.cause = "Authenticated User is null";
		return error;
	}

	public static AuthUserError nullEmailError() {
		AuthUserError error = new AuthUserError();
		error.error = "NullEmailError";
		error.cause = "Email in authenticated user is empty";
		return error;
	}

	public static AuthUserError userNotFoundError() {
		AuthUserError error = new AuthUserError();
		error.error = "UserNotFoundError";
		error.cause = "User not found";
		return error;
	}

	public static AuthUserError userAnonymityError() {
		AuthUserError error = new AuthUserError();
		error.error = "UserAnonymityError";
		error.cause = "User is anonymous";
		return error;
	}
}
