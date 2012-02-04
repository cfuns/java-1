package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class H1Widget extends TagWidget {

	private static final String TAG = "h1";

	public H1Widget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

}
