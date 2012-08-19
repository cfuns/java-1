package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class SingleTagWidget implements Widget {

	private final String tag;

	private final Map<String, String> attributes = new HashMap<String, String>();

	public SingleTagWidget(final String tag) {
		this.tag = tag;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.print("<");
		out.print(getTag());
		for (final Entry<String, String> e : attributes.entrySet()) {
			out.print(" ");
			out.print(StringEscapeUtils.escapeHtml(e.getKey()));
			out.print("=\"");
			out.print(StringEscapeUtils.escapeHtml(e.getValue()));
			out.print("\"");
		}
		out.print("/>");
	}

	public SingleTagWidget addAttribute(final String name, final String value) {
		attributes.put(name, value);
		return this;
	}

	public String getTag() {
		return tag;
	}

}
