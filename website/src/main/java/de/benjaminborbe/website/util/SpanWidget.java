package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class SpanWidget extends TagWidget {

	private static final String TAG = "span";

	public SpanWidget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

	public SpanWidget(final String content) {
		this(new StringWidget(content));
	}

	public SpanWidget() {
		super(TAG);
	}
}
