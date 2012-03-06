package de.benjaminborbe.dhl.gui.widget;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.website.link.LinkRelativWidget;

public class DhlGuiCreateDhlIdentifierLink extends LinkRelativWidget {

	private static final String PATH = "/dhl/create";

	private static final String CONTENT = "add tracking";

	public DhlGuiCreateDhlIdentifierLink(final HttpServletRequest request) throws MalformedURLException {
		super(request, PATH, CONTENT);
	}

}
