package de.benjaminborbe.bookmark.gui.widget;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.website.link.LinkRelativWidget;

public class BookmarkCreateLink extends LinkRelativWidget {

	private static final String path = "/bookmark/create";

	private static final String content = "add bookmark";

	public BookmarkCreateLink(final HttpServletRequest request) throws MalformedURLException {
		super(request, path, content);
	}

}
