package de.benjaminborbe.crawler.api;

public interface CrawlerService {

	void crawleDomain(String domainUrl) throws CrawlerException;
}
