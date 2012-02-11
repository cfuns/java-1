package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class H3Widget extends TagWidget {

	private static final String TAG = "h3";

	public H3Widget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public H3Widget(final String content) {
		this(new StringWidget(content));
	}
}
