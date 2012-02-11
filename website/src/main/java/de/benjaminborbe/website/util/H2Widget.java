package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class H2Widget extends TagWidget {

	private static final String TAG = "h2";

	public H2Widget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public H2Widget(final String content) {
		this(new StringWidget(content));
	}

}
