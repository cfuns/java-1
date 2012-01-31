package de.benjaminborbe.index.api;

import java.net.URL;

public interface IndexerService {

	void addToIndex(String index, URL url, String title, String content);
}
