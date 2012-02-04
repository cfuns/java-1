package de.benjaminborbe.website.link;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import de.benjaminborbe.html.api.Widget;

public class LinkRelativWidget extends LinkWidget {

	public LinkRelativWidget(final HttpServletRequest request, final String path, final Widget contentWidget) throws MalformedURLException {
		super(buildUrl(request, path), contentWidget);
	}

	protected static URL buildUrl(final HttpServletRequest request, final String path) throws MalformedURLException {
		final StringWriter url = new StringWriter();
		url.append(request.getScheme());
		url.append("://");
		url.append(request.getServerName());
		url.append(request.getContextPath());
		url.append(path);
		return new URL(url.toString());
	}

}
