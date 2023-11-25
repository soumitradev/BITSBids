package com.jamers.BITSBids.response_types.errors;

public class UserCreateError extends GenericError {
	public String error;
	public String cause;

	public static UserCreateError nullUserError() {
		UserCreateError error = new UserCreateError();
		error.error = "NullUserError";
		error.cause = "UserCreateData is null";
		return error;
	}

	public static UserCreateError nullEmailError() {
		UserCreateError error = new UserCreateError();
		error.error = "NullEmailError";
		error.cause = "Email in UserCreateData is null";
		return error;
	}

	public static UserCreateError nullNameError() {
		UserCreateError error = new UserCreateError();
		error.error = "NullNameError";
		error.cause = "Name in UserCreateData is null";
		return error;
	}

	public static UserCreateError userAlreadyExistsError() {
		UserCreateError error = new UserCreateError();
		error.error = "UserAlreadyExistsError";
		error.cause = "User already exists";
		return error;
	}

	public static UserCreateError invalidEmailError() {
		UserCreateError error = new UserCreateError();
		error.error = "InvalidEmailError";
		error.cause = "You are not authorized to access this resource";
		return error;
	}
}
