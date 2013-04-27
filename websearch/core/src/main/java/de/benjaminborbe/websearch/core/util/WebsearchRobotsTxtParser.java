package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.tools.util.LineIterator;

public class WebsearchRobotsTxtParser {

	private static final String DISALLOW_STRING = "Disallow:";

	private static final String UA_STRING = "User-agent:";

	public WebsearchRobotsTxt parseRobotsTxt(final String content) {
		final WebsearchRobotsTxt robotsTxt = new WebsearchRobotsTxt();

		String ua = null;
		final LineIterator li = new LineIterator(content);
		while (li.hasNext()) {
			final String line = li.next();
			if (!line.startsWith("#")) {
				if (line.startsWith(UA_STRING)) {
					final int pos = line.indexOf(UA_STRING);
					ua = line.substring(pos + UA_STRING.length()).trim();
				}
				if (ua != null && line.startsWith(DISALLOW_STRING)) {
					final int pos = line.indexOf(DISALLOW_STRING);
					final String uri = line.substring(pos + DISALLOW_STRING.length()).trim();
					if (!uri.isEmpty()) {
						robotsTxt.add(ua, uri);
					}
				}
			}
		}

		return robotsTxt;
	}
}
