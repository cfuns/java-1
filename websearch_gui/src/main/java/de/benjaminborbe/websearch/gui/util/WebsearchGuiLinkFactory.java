package de.benjaminborbe.websearch.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
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

	public Widget pageList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_LIST, "list pages");
	}

	public Widget pageRefresh(final HttpServletRequest request, final WebsearchPageIdentifier pageIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_REFRESH_PAGE, new MapParameter().add(
				WebsearchGuiConstants.PARAMETER_PAGE_ID, String.valueOf(pageIdentifier)), "refresh page");
	}

	public Widget pageExpire(final HttpServletRequest request, final WebsearchPageIdentifier pageIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/websearch/expire", new MapParameter().add(WebsearchGuiConstants.PARAMETER_PAGE_ID, String.valueOf(pageIdentifier)), "expire ");
	}

	public Widget configurationUpdate(final HttpServletRequest request, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_UPDATE, new MapParameter().add(
				WebsearchGuiConstants.PARAMETER_CONFIGURATION_ID, String.valueOf(websearchConfigurationIdentifier)), "edit");
	}

	public Widget configurationDelete(final HttpServletRequest request, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_DELETE, new MapParameter().add(
				WebsearchGuiConstants.PARAMETER_CONFIGURATION_ID, String.valueOf(websearchConfigurationIdentifier)), "delete").addConfirm("delete?");
	}

	public Widget configurationCreate(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_CREATE, "create configuration");
	}

	public String configurationListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_LIST;
	}

}
