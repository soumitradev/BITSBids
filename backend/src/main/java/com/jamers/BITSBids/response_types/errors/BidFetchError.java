package com.jamers.BITSBids.response_types.errors;

public class BidFetchError extends GenericError {
	public static BidFetchError invalidBidError() {
		BidFetchError error = new BidFetchError();
		error.error = "InvalidBidError";
		error.cause = "Mismatch in bids and products";
		return error;
	}
}
