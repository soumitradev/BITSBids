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
}
