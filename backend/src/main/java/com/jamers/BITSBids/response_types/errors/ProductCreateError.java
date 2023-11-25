package com.jamers.BITSBids.response_types.errors;

public class ProductCreateError extends GenericError {
	public String error;
	public String cause;

	public static ProductCreateError nullProductError() {
		ProductCreateError error = new ProductCreateError();
		error.error = "NullProductError";
		error.cause = "ProductCreateData is null";
		return error;
	}
}
