package com.jamers.BITSBids.response_types.errors;

public class MessageCreateError extends GenericError {
	public static MessageCreateError nullMessageError() {
		MessageCreateError error = new MessageCreateError();
		error.error = "NullMessageError";
		error.cause = "MessageCreateData is null";
		return error;
	}

	public static MessageCreateError emptyMessageError() {
		MessageCreateError error = new MessageCreateError();
		error.error = "EmptyMessageError";
		error.cause = "Message is empty";
		return error;
	}

	public static MessageCreateError internalServerError() {
		MessageCreateError error = new MessageCreateError();
		error.error = "InternalServerError";
		error.cause = "Something went wrong while sending message";
		return error;
	}
}
