package de.benjaminborbe.crawler.api;

import java.net.URL;

public interface CrawlerInstruction {

	URL getUrl();

	int getTimeout();

	long getDepth();
}
