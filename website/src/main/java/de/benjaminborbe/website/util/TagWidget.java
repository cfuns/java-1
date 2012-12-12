package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.util.ComparatorBase;

public class TagWidget implements Widget {

	private final class EntrySetComparator extends ComparatorBase<Entry<String, String>, String> {

		@Override
		public String getValue(final Entry<String, String> o) {
			return o.getKey();
		}
	}

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

	public TagWidget addAttribute(final String key, final Object value) {
		attributes.put(key, value != null ? String.valueOf(value) : "");
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
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.print("<");
		out.print(tag);

		for (final Entry<String, String> e : sortEntrySet(attributes.entrySet())) {
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

	private List<Entry<String, String>> sortEntrySet(final Set<Entry<String, String>> entrySet) {
		final List<Entry<String, String>> result = new ArrayList<Entry<String, String>>(entrySet);
		Collections.sort(result, new EntrySetComparator());
		return result;
	}

	public void addContent(final String content) {
		addContent(new StringWidget(content));
	}
}
