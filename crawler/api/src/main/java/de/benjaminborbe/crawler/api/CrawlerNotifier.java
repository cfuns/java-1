package de.benjaminborbe.crawler.api;

import de.benjaminborbe.httpdownloader.api.HttpResponse;

public interface CrawlerNotifier {

	void notifiy(HttpResponse httpResponse);
}
