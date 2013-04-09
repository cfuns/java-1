package de.benjaminborbe.dhl.gui.widget;

import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

public class DhlGuiCreateDhlIdentifierLink extends LinkRelativWidget {

	private static final String PATH = "/dhl/create";

	private static final String CONTENT = "add tracking";

	public DhlGuiCreateDhlIdentifierLink(final HttpServletRequest request) throws MalformedURLException {
		super(request, PATH, CONTENT);
	}

}
