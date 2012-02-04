package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class TagWidget implements Widget {

	private final String tag;

	private final Widget contentWidget;

	public TagWidget(final String tag, final Widget contentWidget) {
		this.tag = tag;
		this.contentWidget = contentWidget;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.print("<");
		out.print(tag);
		out.print(">");
		contentWidget.render(request, response, context);
		out.print("</");
		out.print(tag);
		out.print(">");
	}

}
