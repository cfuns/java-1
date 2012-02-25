package de.benjaminborbe.website.servlet;

public class RedirectException extends Exception {

	private static final long serialVersionUID = 1610935027656175136L;

	private final String target;

	public RedirectException(final String target) {
		this.target = target;
	}

	public String getTarget() {
		return target;
	}
}
