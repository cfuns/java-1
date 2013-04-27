package de.benjaminborbe.website.link;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.StringWidget;

import javax.servlet.http.HttpServletRequest;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class LinkRelativWidget extends LinkWidget {

	public LinkRelativWidget(final HttpServletRequest request, final String path, final Widget contentWidget) throws MalformedURLException {
		super(buildUrl(request, path), contentWidget);
	}

	public LinkRelativWidget(final HttpServletRequest request, final String path, final String content) throws MalformedURLException {
		this(request, path, new StringWidget(content));
	}

	public LinkRelativWidget(
		final UrlUtil urlUtil,
		final HttpServletRequest request,
		final String path,
		final Map<String, String[]> parameter,
		final Widget contentWidget
	)
		throws MalformedURLException,
		UnsupportedEncodingException {
		super(buildUrl(request, urlUtil.buildUrl(path, parameter)), contentWidget);
	}

	public LinkRelativWidget(
		final UrlUtil urlUtil,
		final HttpServletRequest request,
		final String path,
		final Map<String, String[]> parameter,
		final String content
	)
		throws MalformedURLException,
		UnsupportedEncodingException {
		this(request, urlUtil.buildUrl(path, parameter), new StringWidget(content));
	}

	protected static URL buildUrl(final HttpServletRequest request, final String path) throws MalformedURLException {
		final StringWriter url = new StringWriter();
		url.append(request.getScheme());
		url.append("://");
		url.append(request.getServerName());
		if (!("http".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 80 || "https".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 443)) {
			url.append(":");
			url.append(String.valueOf(request.getServerPort()));
		}
		url.append(request.getContextPath());
		url.append(path.replaceAll("//", "/"));
		return new URL(url.toString());
	}
}
