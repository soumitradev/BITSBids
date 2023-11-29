package com.jamers.BITSBids.response_types.errors;

public class ProductCreateError extends GenericError {
	public static ProductCreateError nullProductError() {
		ProductCreateError error = new ProductCreateError();
		error.error = "NullProductError";
		error.cause = "ProductCreateData is null";
		return error;
	}

	public static ProductCreateError invalidCategoryError() {
		ProductCreateError error = new ProductCreateError();
		error.error = "invalidCategoryError";
		error.cause = "Category is invalid";
		return error;
	}
}
