package de.benjaminborbe.crawler.api;

import de.benjaminborbe.httpdownloader.api.HttpResponse;

public interface CrawlerNotifierResult extends HttpResponse {

	Long getDepth();

	Integer getTimeout();
}
