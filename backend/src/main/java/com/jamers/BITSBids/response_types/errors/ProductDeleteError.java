package com.jamers.BITSBids.response_types.errors;

public class ProductDeleteError extends GenericError {
	public static ProductDeleteError notProductSellerError() {
		ProductDeleteError error = new ProductDeleteError();
		error.error = "NotProductSellerError";
		error.cause = "User is unauthorized to delete Product";
		return error;
	}
}

