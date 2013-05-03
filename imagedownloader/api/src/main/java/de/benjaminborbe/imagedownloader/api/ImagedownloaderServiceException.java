package de.benjaminborbe.imagedownloader.api;

public class ImagedownloaderServiceException extends Exception {

	private static final long serialVersionUID = -8111865762242405201L;

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
