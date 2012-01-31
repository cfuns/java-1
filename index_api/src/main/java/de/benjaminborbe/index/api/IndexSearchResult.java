package de.benjaminborbe.index.api;

import java.net.URL;

public interface IndexSearchResult {

	String getIndex();

	String getTitle();

	String getContent();

	URL getURL();
}
