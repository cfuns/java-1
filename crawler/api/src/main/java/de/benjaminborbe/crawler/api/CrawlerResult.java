package de.benjaminborbe.crawler.api;

import java.net.URL;

public interface CrawlerResult {

	URL getUrl();

	String getContent();

	String getContentType();

	boolean isAvailable();
}
