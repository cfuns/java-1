package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.util.ComparatorBase;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SingleTagWidget implements Widget {

	private final class EntrySetComparator extends ComparatorBase<Entry<String, String>, String> {

		@Override
		public String getValue(final Entry<String, String> o) {
			return o.getKey();
		}
	}

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
		for (final Entry<String, String> e : sortEntrySet(attributes.entrySet())) {
			out.print(" ");
			out.print(StringEscapeUtils.escapeHtml(e.getKey()));
			out.print("=\"");
			out.print(StringEscapeUtils.escapeHtml(e.getValue() != null ? e.getValue() : ""));
			out.print("\"");
		}
		out.print("/>");
	}

	private List<Entry<String, String>> sortEntrySet(final Set<Entry<String, String>> entrySet) {
		final List<Entry<String, String>> result = new ArrayList<Entry<String, String>>(entrySet);
		Collections.sort(result, new EntrySetComparator());
		return result;
	}

	public SingleTagWidget addAttribute(final String name, final Object value) {
		attributes.put(name, value != null ? String.valueOf(value) : "");
		return this;
	}

	public String getTag() {
		return tag;
	}

	public String getAttribute(final String name) {
		return attributes.get(name);
	}

}
