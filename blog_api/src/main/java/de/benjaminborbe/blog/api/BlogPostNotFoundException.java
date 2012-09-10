package de.benjaminborbe.blog.api;

public class BlogPostNotFoundException extends Exception {

	private static final long serialVersionUID = -3762421767470471656L;

	public BlogPostNotFoundException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public BlogPostNotFoundException(final String arg0) {
		super(arg0);
	}

	public BlogPostNotFoundException(final Throwable arg0) {
		super(arg0);
	}

}
