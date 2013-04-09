package de.benjaminborbe.dhl.gui.widget;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.net.MalformedURLException;

public class DhlGuiDeleteDhlIdentifierLink extends LinkRelativWidget {

	private static final String PATH = "/dhl/delete";

	private static final String CONTENT = "delete";

	public DhlGuiDeleteDhlIdentifierLink(final HttpServletRequest request, final DhlIdentifier dhlIdentifier) throws MalformedURLException {
		super(request, buildPath(dhlIdentifier), CONTENT);
	}

	private static String buildPath(final DhlIdentifier dhlIdentifier) {
		final StringWriter sw = new StringWriter();
		sw.append(PATH);
		sw.append("?id=");
		sw.append(String.valueOf(dhlIdentifier.getId()));
		return sw.toString();
	}

}
