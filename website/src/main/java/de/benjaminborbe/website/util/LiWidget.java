package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class LiWidget extends TagWidget {

	private static final String TAG = "li";

	public LiWidget(final Widget contentWidget) {
		super(TAG, contentWidget);
	}

}
