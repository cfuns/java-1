package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class PreWidget extends TagWidget {

	private final static String tag = "pre";

	public PreWidget(final String content) {
		super(tag, content);
	}

	public PreWidget(final Widget contentWidget) {
		super(tag, contentWidget);
	}

	public PreWidget() {
		super(tag);
	}

}
