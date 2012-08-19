package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class TagWidget implements Widget {

	private final Map<String, String> attributes = new HashMap<String, String>();

	private final String tag;

	private Widget contentWidget;

	public TagWidget(final String tag) {
		this.tag = tag;
	}

	public TagWidget(final String tag, final Widget contentWidget) {
		this.tag = tag;
		this.contentWidget = contentWidget;
	}

	public TagWidget(final String tag, final String content) {
		this(tag, new StringWidget(content));
	}

	public TagWidget addAttribute(final String key, final String value) {
		attributes.put(key, value);
		return this;
	}

	public String getAttribute(final String name) {
		return attributes.get(name);
	}

	public TagWidget addContent(final Widget contentWidget) {
		this.contentWidget = contentWidget;
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		final PrintWriter out = response.getWriter();
		out.print("<");
		out.print(tag);

		for (final Entry<String, String> e : attributes.entrySet()) {
			out.print(" ");
			out.print(StringEscapeUtils.escapeHtml(e.getKey()));
			out.print("=\"");
			out.print(StringEscapeUtils.escapeHtml(e.getValue()));
			out.print("\"");
		}

		out.print(">");
		if (contentWidget != null) {
			contentWidget.render(request, response, context);
		}
		out.print("</");
		out.print(tag);
		out.print(">");
	}
}
