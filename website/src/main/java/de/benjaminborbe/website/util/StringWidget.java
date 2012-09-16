package de.benjaminborbe.website.util;

import org.apache.commons.lang.StringEscapeUtils;

public class StringWidget extends HtmlContentWidget {

	public StringWidget(final String content) {
		super(StringEscapeUtils.escapeHtml(content));
	}

}
