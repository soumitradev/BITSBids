package com.jamers.BITSBids.response_types.errors;

public class CategoryFetchError extends GenericError {
	public static CategoryFetchError invalidCategoryError() {
		CategoryFetchError error = new CategoryFetchError();
		error.error = "InvalidCategoryError";
		error.cause = "Requested Category doesn't exist";
		return error;
	}
}
