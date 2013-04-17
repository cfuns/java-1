package de.benjaminborbe.wiki.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.wiki.render.part.Part;
import de.benjaminborbe.wiki.render.part.StringPart;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WikiConfluenceRenderer implements WikiRenderer {

	private final Logger logger;

	@Inject
	public WikiConfluenceRenderer(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String render(final String markup) {
		logger.debug("render");
		final Headlines headlines = new Headlines();
		final List<Part> parts = new ArrayList<>();
		final String[] lines = markup.split("\n");
		boolean codeOpen = false;
		boolean ulOpen = false;
		boolean emptyLine = false;
		boolean simpleText = false;
		for (final String line : lines) {
			final String lineLowerCase = line.toLowerCase().trim();

			if (lineLowerCase.equals("{code}")) {
				if (codeOpen) {
					parts.add(new StringPart("</pre>\n"));
					codeOpen = false;
				} else {
					parts.add(new StringPart("\n<pre class=\"wikiCode\">\n"));
					codeOpen = true;
					simpleText = false;
				}
				continue;
			}

			if (codeOpen) {
				parts.add(new StringPart(line + "\n"));
				continue;
			}

			if (line.indexOf("* ") == 0) {
				if (!ulOpen) {
					parts.add(new StringPart("<ul class=\"wikiList\">"));
					ulOpen = true;
				}
				parts.add(new StringPart("<li>"));
				parts.add(new StringPart(line.substring(2)));
				parts.add(new StringPart("</li>"));
				continue;
			} else {
				if (ulOpen) {
					parts.add(new StringPart("</ul>"));
					ulOpen = false;
				}
			}

			int pos;
			if (lineLowerCase.trim().isEmpty()) {
				if (simpleText) {
					emptyLine = true;
				}
				simpleText = false;
			} else if ((pos = lineLowerCase.indexOf("h1.")) != -1) {
				final String title = line.substring(pos + 3).trim();
				parts.add(headlines.createHead1Part(title));
				emptyLine = false;
				simpleText = false;
			} else if ((pos = lineLowerCase.indexOf("h2.")) != -1) {
				final String title = line.substring(pos + 3).trim();
				parts.add(headlines.createHead2Part(title));
				emptyLine = false;
				simpleText = false;
			} else if ((pos = lineLowerCase.indexOf("h3.")) != -1) {
				final String title = line.substring(pos + 3).trim();
				parts.add(headlines.createHead3Part(title));
				emptyLine = false;
				simpleText = false;
			} else if ((pos = lineLowerCase.indexOf("{toc}")) != -1) {
				parts.add(headlines.createTocPart());
				emptyLine = false;
				simpleText = false;
			} else {
				if (emptyLine) {
					parts.add(new StringPart("<br/><br/>"));
					emptyLine = false;
				}
				if (simpleText) {
					parts.add(new StringPart(" "));
				}
				simpleText = true;
				parts.add(new StringPart(line.trim()));
			}
		}
		if (ulOpen) {
			parts.add(new StringPart("</ul>"));
		}
		final StringWriter result = new StringWriter();
		for (final Part part : parts) {
			result.append(part.asString());
		}
		return result.toString();
	}
}
