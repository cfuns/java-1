package de.benjaminborbe.wiki.render;

public class WikiRendererNotFoundException extends Exception {

	private static final long serialVersionUID = -3280667643500070157L;

	public WikiRendererNotFoundException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public WikiRendererNotFoundException(final String arg0) {
		super(arg0);
	}

	public WikiRendererNotFoundException(final Throwable arg0) {
		super(arg0);
	}

}
