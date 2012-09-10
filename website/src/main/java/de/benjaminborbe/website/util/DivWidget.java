package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class DivWidget extends TagWidget {

	private static final String TAG = "div";

	public DivWidget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public DivWidget(final String content) {
		this(new StringWidget(content));
	}
}
