package de.benjaminborbe.search.api;

import java.net.URL;

public interface SearchResult {

	String getType();

	String getTitle();

	URL getUrl();

	String getDescription();
}
