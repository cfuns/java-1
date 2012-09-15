package de.benjaminborbe.wiki.render.part;

import java.io.StringWriter;
import java.util.List;

public class TocPart implements Part {

	private final List<Head1Part> h1s;

	public TocPart(final List<Head1Part> h1s) {
		this.h1s = h1s;
	}

	@Override
	public String asString() {
		final StringWriter sw = new StringWriter();
		if (h1s.size() > 0) {
			sw.append("<ul>");
			for (final Head1Part h1 : h1s) {
				sw.append("<li>");
				sw.append(h1.getTitle());
				sw.append("</li>");
			}
			sw.append("</ul>");
		}
		return sw.toString();
	}
}
