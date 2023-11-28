package com.jamers.BITSBids.response_types.errors;

public class UserEditError extends GenericError {
	public String error;
	public String cause;

	public static UserEditError nullFieldsError() {
		UserEditError error = new UserEditError();
		error.error = "NullFieldsError";
		error.cause = "Input Fields are null";
		return error;
	}


}