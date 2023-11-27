package com.jamers.BITSBids.response_types.errors;

public class ProductFetchError {
	public String error;
	public String cause;

	public static ProductDeleteError invalidProductError() {
		ProductDeleteError error = new ProductDeleteError();
		error.error = "InvalidProductError";
		error.cause = "Requested Product doesn't exist";
		return error;
	}
}
