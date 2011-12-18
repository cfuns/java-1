package de.benjaminborbe.index.api;

import java.net.URL;

public interface Indexer {

	void addToIndex(String index, URL url, String title, String content);
}
