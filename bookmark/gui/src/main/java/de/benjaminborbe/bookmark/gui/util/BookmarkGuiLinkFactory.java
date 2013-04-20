package de.benjaminborbe.bookmark.gui.util;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;

import de.benjaminborbe.bookmark.gui.BookmarkGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class BookmarkGuiLinkFactory {

	@Inject
	public BookmarkGuiLinkFactory() {
		super();
	}

	public Widget listBookmarks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + BookmarkGuiConstants.NAME + BookmarkGuiConstants.URL_LIST, "list");
	}

}
