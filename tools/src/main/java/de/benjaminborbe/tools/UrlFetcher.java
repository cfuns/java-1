package de.benjaminborbe.tools;

import java.net.URL;

public interface UrlFetcher {

	String getContentAsString(final URL url);
}
