package de.benjaminborbe.website.util;

public class ListWidget extends HtmlListWidget {

	@Override
	public HtmlListWidget add(final String content) {
		return add(new StringWidget(content));
	}

}
