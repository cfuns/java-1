package de.benjaminborbe.crawler.api;

import de.benjaminborbe.httpdownloader.api.HttpResponse;

public interface CrawlerService {

	void processCrawlerInstruction(CrawlerInstruction crawlerInstruction) throws CrawlerException;

	void notify(HttpResponse httpResponse) throws CrawlerException;
}
