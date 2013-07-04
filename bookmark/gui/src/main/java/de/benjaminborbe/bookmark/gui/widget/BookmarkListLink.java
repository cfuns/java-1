package de.benjaminborbe.bookmark.gui.widget;

import de.benjaminborbe.bookmark.gui.BookmarkGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class BookmarkListLink extends LinkRelativWidget {

	private static final String path = "/" + BookmarkGuiConstants.NAME + BookmarkGuiConstants.URL_LIST;

	private static final String content = "bookmark list";

	public BookmarkListLink(final HttpServletRequest request) throws MalformedURLException {
		super(request, path, content);
	}

}
