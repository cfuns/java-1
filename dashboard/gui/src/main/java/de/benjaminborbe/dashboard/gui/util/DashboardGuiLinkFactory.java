package de.benjaminborbe.dashboard.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;

import de.benjaminborbe.dashboard.gui.DashboardGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DashboardGuiLinkFactory {

	@Inject
	public DashboardGuiLinkFactory() {
	}

	public Widget configure(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(request, "/" + DashboardGuiConstants.NAME + DashboardGuiConstants.URL_CONFIGURE, "configure");
	}

}
