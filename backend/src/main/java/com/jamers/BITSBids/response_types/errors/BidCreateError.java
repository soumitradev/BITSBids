package com.jamers.BITSBids.response_types.errors;

public class BidCreateError {
		public String error;
		public String cause;

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

	}

