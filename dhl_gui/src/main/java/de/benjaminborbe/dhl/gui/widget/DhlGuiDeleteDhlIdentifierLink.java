package de.benjaminborbe.dhl.gui.widget;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DhlGuiDeleteDhlIdentifierLink extends LinkRelativWidget {

	private static final String PATH = "/dhl/delete?id=";

	private static final String CONTENT = "delete";

	public DhlGuiDeleteDhlIdentifierLink(final HttpServletRequest request, final DhlIdentifier dhlIdentifier) throws MalformedURLException {
		super(request, buildPath(dhlIdentifier), CONTENT);
	}

	private static String buildPath(final DhlIdentifier dhlIdentifier) {
		return PATH + "?id=" + dhlIdentifier.getId() + "&zip=" + dhlIdentifier.getZip();
	}

}
