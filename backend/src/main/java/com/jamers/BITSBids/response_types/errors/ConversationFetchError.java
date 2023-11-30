package com.jamers.BITSBids.response_types.errors;

public class ConversationFetchError extends GenericError {
	public static ConversationFetchError invalidConversationError() {
		ConversationFetchError error = new ConversationFetchError();
		error.error = "InvalidConversationError";
		error.cause = "Requested conversation doesn't exist";
		return error;
	}
}
