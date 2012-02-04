package de.benjaminborbe.website.link;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.util.StringWidget;

public class LinkWidget implements Widget {

	private final URL url;

	private final Widget contentWidget;

	private Target target;

	public LinkWidget(final URL url, final Widget contentWidget) {
		this.url = url;
		this.contentWidget = contentWidget;
	}

	public LinkWidget(final URL url, final String content) {
		this(url, new StringWidget(content));
	}

	public LinkWidget addTarget(final Target target) {
		this.target = target;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.print("<a href=\"" + url.toExternalForm() + "\"");
		if (target != null) {
			out.print(" target=\"" + target + "\"");
		}
		out.print(">");
		contentWidget.render(request, response, context);
		out.print("</a>");
	}
}
