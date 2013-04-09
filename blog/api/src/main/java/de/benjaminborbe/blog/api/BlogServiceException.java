package de.benjaminborbe.blog.api;

public class BlogServiceException extends Exception {

	private static final long serialVersionUID = 2795832863650273286L;

	public BlogServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public BlogServiceException(final String arg0) {
		super(arg0);
	}

	public BlogServiceException(final Throwable arg0) {
		super(arg0);
	}

}
