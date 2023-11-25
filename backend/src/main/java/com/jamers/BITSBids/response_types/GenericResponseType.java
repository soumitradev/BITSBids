package com.jamers.BITSBids.response_types;

public class GenericResponseType {
	public Object data;
	public ResponseStatus status;

	public GenericResponseType(Object data, ResponseStatus status) {
		this.data = data;
		this.status = status;
	}

	public enum ResponseStatus {
		SUCCESS, ERROR,
	}
}
