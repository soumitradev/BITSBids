package com.jamers.BITSBids.response_types.errors;

public class ProductDeleteError {
	public String error;
	public String cause;

	public static ProductDeleteError notProductSellerError() {
		ProductDeleteError error = new ProductDeleteError();
		error.error = "notProductSellerError";
		error.cause = "User is unauthorized to delete Product";
		return error;
	}
}

