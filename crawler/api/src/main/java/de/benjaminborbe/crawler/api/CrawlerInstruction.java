package de.benjaminborbe.crawler.api;

import java.net.URL;

public interface CrawlerInstruction {

	URL getUrl();

	Integer getTimeout();

	Long getDepth();
}
