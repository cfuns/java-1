package de.benjaminborbe.websearch.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.websearch.api.WebsearchConfiguration;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class WebsearchGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public WebsearchGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget listPages(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_LIST, "list pages");
	}

	public Widget refreshPage(final HttpServletRequest request, final WebsearchPageIdentifier pageIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_REFRESH_PAGE, new MapParameter().add(
				WebsearchGuiConstants.PARAMETER_PAGE_ID, String.valueOf(pageIdentifier)), "refresh page");
	}

	public Widget expirePage(final HttpServletRequest request, final WebsearchPageIdentifier pageIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/websearch/expire", new MapParameter().add(WebsearchGuiConstants.PARAMETER_PAGE_ID, String.valueOf(pageIdentifier)), "expire ");
	}

	public String updateConfiguration(final HttpServletRequest request, final WebsearchConfigurationIdentifier id) {
		return null;
	}

	public String collectionListUrl(final HttpServletRequest request, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier) {
		return null;
	}

	public String listCollections(final HttpServletRequest request, final WebsearchConfiguration websearchConfiguration) {
		return null;
	}

	public String deleteConfiguration(final HttpServletRequest request, final WebsearchConfigurationIdentifier id) {
		return null;
	}

	public String createConfiguration(final HttpServletRequest request) {
		return null;
	}

}
