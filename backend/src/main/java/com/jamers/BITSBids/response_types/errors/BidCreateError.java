package com.jamers.BITSBids.response_types.errors;

public class BidCreateError extends GenericError {
	public static BidCreateError invalidBidError() {
		BidCreateError error = new BidCreateError();
		error.error = "InvalidBidError";
		error.cause = "Bid can't be lower than Ongoing price";
		return error;
	}

	public static BidCreateError nullBidError() {
		BidCreateError error = new BidCreateError();
		error.error = "NullBidError";
		error.cause = "BidCreateData is null";
		return error;
	}

	public static BidCreateError internalServerError() {
		BidCreateError error = new BidCreateError();
		error.error = "InternalServerError";
		error.cause = "Internal server error while making bid";
		return error;
	}

	public static BidCreateError ProductSoldError() {
		BidCreateError error = new BidCreateError();
		error.error = "ProductSoldError";
		error.cause = "This product is already sold.";
		return error;
	}
}

