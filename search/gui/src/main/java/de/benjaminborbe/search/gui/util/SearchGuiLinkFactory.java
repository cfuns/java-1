package de.benjaminborbe.search.gui.util;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.search.gui.SearchGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class SearchGuiLinkFactory {

	public Widget searchHelp(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + SearchGuiConstants.NAME + SearchGuiConstants.URL_SEARCH_HELP, "help");
	}

	public String suggestUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + SearchGuiConstants.NAME + SearchGuiConstants.URL_SEARCH_SUGGEST;
	}

	public String searchUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + SearchGuiConstants.NAME + SearchGuiConstants.URL_SEARCH;
	}

}
