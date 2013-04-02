package de.benjaminborbe.wiki.render.part;

import java.io.StringWriter;
import java.util.List;

public class TocPart implements Part {

	private final List<Head1Part> h1s;

	private final int minLevel;

	public TocPart(final List<Head1Part> h1s, final int minLevel) {
		this.h1s = h1s;
		this.minLevel = minLevel;
	}

	public TocPart(final List<Head1Part> h1s) {
		this(h1s, 1);
	}

	@Override
	public String asString() {
		final StringWriter sw = new StringWriter();
		if (h1s.size() > 0) {
			if (minLevel >= 1) {
				sw.append("<ul class=\"wikiToc\">");
			}
			for (final Head1Part h1 : h1s) {
				if (minLevel >= 1) {
					sw.append("<li>");
					sw.append("<a href=\"#" + h1.getTitle() + "\">");
					sw.append(h1.getTitle());
					sw.append("</a>");
				}
				final List<Head2Part> h2s = h1.getHead2Parts();
				if (h2s.size() > 0) {
					if (minLevel >= 2) {
						sw.append("<ul>");
					}
					for (final Head2Part h2 : h2s) {
						if (minLevel >= 2) {
							sw.append("<li>");
							sw.append("<a href=\"#" + h1.getTitle() + "-" + h2.getTitle() + "\">");
							sw.append(h2.getTitle());
							sw.append("</a>");
						}
						final List<Head3Part> h3s = h2.getHead3Parts();
						if (h3s.size() > 0) {
							sw.append("<ul>");
							for (final Head3Part h3 : h3s) {
								sw.append("<li>");
								sw.append("<a href=\"#" + h1.getTitle() + "-" + h2.getTitle() + "\">");
								sw.append(h3.getTitle());
								sw.append("</a>");
								sw.append("</li>");
							}
							sw.append("</ul>");
						}
						if (minLevel >= 2) {
							sw.append("</li>");
						}
					}
					if (minLevel >= 2) {
						sw.append("</ul>");
					}
				}
				if (minLevel >= 1) {
					sw.append("</li>");
				}
			}
			if (minLevel >= 1) {
				sw.append("</ul>");
			}
		}
		return sw.toString();
	}
}
