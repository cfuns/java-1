package de.benjaminborbe.imagedownloader.api;

public class ImagedownloaderServiceException extends Exception {

	public ImagedownloaderServiceException(final Throwable cause) {
		super(cause);
	}

	public ImagedownloaderServiceException(final String message) {
		super(message);
	}

	public ImagedownloaderServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
