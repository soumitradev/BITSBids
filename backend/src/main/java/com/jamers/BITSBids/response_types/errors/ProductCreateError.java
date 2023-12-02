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
		error.error = "InvalidCategoryError";
		error.cause = "Category is invalid";
		return error;
	}

	public static ProductCreateError meiliServerError() {
		ProductCreateError error = new ProductCreateError();
		error.error = "MeiliServerError";
		error.cause = "Error adding product to MeiliSearch";
		return error;
	}
}
