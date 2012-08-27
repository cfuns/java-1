package de.benjaminborbe.wiki.render;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class WikiConfluenceRenderer implements WikiRenderer {

	@Inject
	public WikiConfluenceRenderer() {
	}

	@Override
	public String render(final String markup) {
		final StringBuffer result = new StringBuffer();
		final String[] lines = markup.split("\n");
		for (final String line : lines) {
			final String lineLowerCase = line.toLowerCase();
			int pos;
			if ((pos = lineLowerCase.indexOf("h1.")) != -1) {
				result.append("<h1>");
				result.append(line.substring(pos + 3).trim());
				result.append("</h1>");
			}
			else if ((pos = lineLowerCase.indexOf("h2.")) != -1) {
				result.append("<h2>");
				result.append(line.substring(pos + 3).trim());
				result.append("</h2>");
			}
			else if ((pos = lineLowerCase.indexOf("h3.")) != -1) {
				result.append("<h3>");
				result.append(line.substring(pos + 3).trim());
				result.append("</h3>");
			}
			else {
				result.append(line);
			}
		}
		return result.toString();
	}

}
