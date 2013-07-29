package de.benjaminborbe.websearch.core.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebsearchRobotsTxt {

	private static final String DEFAULT_UA = "*";

	private final Map<String, Set<String>> useragentUriList = new HashMap<String, Set<String>>();

	public WebsearchRobotsTxt() {
	}

	public boolean isAllowed(final String useragent, final String uri) {
		if (useragentUriList.containsKey(useragent)) {
			final Set<String> uris = useragentUriList.get(useragent);
			for (final String u : uris) {
				if (uri.startsWith(u)) {
					return false;
				}
			}
			return true;
		}
		if (useragentUriList.containsKey(DEFAULT_UA)) {
			final Set<String> uris = useragentUriList.get(DEFAULT_UA);
			for (final String u : uris) {
				if (uri.startsWith(u)) {
					return false;
				}
			}
		}
		return true;
	}

	public void add(final String useragent, final String uri) {
		final Set<String> uris;
		if (useragentUriList.containsKey(useragent)) {
			uris = useragentUriList.get(useragent);
		} else {
			uris = new HashSet<String>();
			useragentUriList.put(useragent, uris);
		}
		uris.add(uri);
	}
}
