package com.jamers.BITSBids.response_types.errors;

public class ProductFetchError {
	public String error;
	public String cause;

	public static ProductFetchError invalidProductError() {
		ProductFetchError error = new ProductFetchError();
		error.error = "InvalidProductError";
		error.cause = "Requested Product doesn't exist";
		return error;
	}
}
