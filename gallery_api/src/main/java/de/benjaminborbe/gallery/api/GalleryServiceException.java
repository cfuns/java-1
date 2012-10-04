package de.benjaminborbe.gallery.api;

public class GalleryServiceException extends Exception {

	private static final long serialVersionUID = -3530289676508064414L;

	public GalleryServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public GalleryServiceException(final String arg0) {
		super(arg0);
	}

	public GalleryServiceException(final Throwable arg0) {
		super(arg0);
	}

}
