package de.benjaminborbe.bookmark.gui.widget;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.bookmark.gui.BookmarkGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class BookmarkListLink extends LinkRelativWidget {

	private static final String path = "/" + BookmarkGuiConstants.NAME + BookmarkGuiConstants.URL_LIST;

	private static final String content = "bookmark list";

	public BookmarkListLink(final HttpServletRequest request) throws MalformedURLException {
		super(request, path, content);
	}

}
