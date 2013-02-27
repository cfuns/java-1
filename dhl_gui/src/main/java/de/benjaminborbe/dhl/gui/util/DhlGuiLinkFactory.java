package de.benjaminborbe.dhl.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.dhl.gui.DhlGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DhlGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public DhlGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget list(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DhlGuiConstants.NAME + DhlGuiConstants.URL_LIST, new MapParameter(), "list");
	}

	public Widget triggerCheck(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DhlGuiConstants.NAME + DhlGuiConstants.URL_TRIGGER_CHECK, new MapParameter(), "trigger check");
	}
}
