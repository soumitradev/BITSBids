package com.jamers.BITSBids.response_types.errors;

public class MessageReadError extends GenericError {
	public static MessageReadError nullMessageError() {
		MessageReadError error = new MessageReadError();
		error.error = "NullMessageError";
		error.cause = "MessageReadData is null";
		return error;
	}

	public static MessageReadError invalidMessageError() {
		MessageReadError error = new MessageReadError();
		error.error = "InvalidMessageError";
		error.cause = "Message is invalid";
		return error;
	}
}
