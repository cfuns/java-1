package de.benjaminborbe.wiki.render;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.render.part.Part;
import de.benjaminborbe.wiki.render.part.StringPart;

@Singleton
public class WikiConfluenceRenderer implements WikiRenderer {

	@Inject
	public WikiConfluenceRenderer() {
	}

	@Override
	public String render(final String markup) {
		final Headlines headlines = new Headlines();
		final List<Part> parts = new ArrayList<Part>();
		final String[] lines = markup.split("\n");
		boolean multilineMode = false;
		boolean ulOpen = false;
		boolean emptyLine = false;
		boolean simpleText = false;
		for (final String line : lines) {
			final String lineLowerCase = line.toLowerCase();
			if (line.indexOf("* ") == 0) {
				multilineMode = true;
				if (!ulOpen) {
					parts.add(new StringPart("<ul>"));
					ulOpen = true;
				}
				parts.add(new StringPart("<li>"));
				parts.add(new StringPart(line.substring(2)));
				parts.add(new StringPart("</li>"));
			}
			else {
				multilineMode = false;
				if (ulOpen) {
					parts.add(new StringPart("</ul>"));
					ulOpen = false;
				}
			}
			if (!multilineMode) {
				int pos;
				if (lineLowerCase.isEmpty()) {
					emptyLine = true;
					simpleText = false;
				}
				else if ((pos = lineLowerCase.indexOf("h1.")) != -1) {
					final String title = line.substring(pos + 3).trim();
					parts.add(headlines.createHead1Part(title));
					emptyLine = false;
					simpleText = false;
				}
				else if ((pos = lineLowerCase.indexOf("h2.")) != -1) {
					final String title = line.substring(pos + 3).trim();
					parts.add(headlines.createHead2Part(title));
					emptyLine = false;
					simpleText = false;
				}
				else if ((pos = lineLowerCase.indexOf("h3.")) != -1) {
					final String title = line.substring(pos + 3).trim();
					parts.add(headlines.createHead3Part(title));
					emptyLine = false;
					simpleText = false;
				}
				else if ((pos = lineLowerCase.indexOf("{toc}")) != -1) {
					parts.add(headlines.createTocPart());
					emptyLine = false;
					simpleText = false;
				}
				else {
					if (emptyLine) {
						parts.add(new StringPart("<br/><br/>"));
						emptyLine = false;
					}
					if (simpleText) {
						parts.add(new StringPart(" "));
					}
					simpleText = true;
					parts.add(new StringPart(line));
				}
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
