package com.jamers.BITSBids.response_types.errors;

public class AuthUserError extends GenericError {
	public String error;
	public String cause;

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

}
