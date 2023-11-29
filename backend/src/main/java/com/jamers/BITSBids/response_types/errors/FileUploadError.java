package com.jamers.BITSBids.response_types.errors;

public class FileUploadError extends GenericError {
	public static FileUploadError fileUploadError() {
		FileUploadError error = new FileUploadError();
		error.error = "FileUploadError";
		error.cause = "Internal server error while uploading file";
		return error;
	}
}
