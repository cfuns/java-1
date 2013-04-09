package de.benjaminborbe.crawler.message;

import de.benjaminborbe.tools.mapper.MapException;

public interface CrawlerMessageMapper {

	String map(CrawlerMessage message) throws MapException;

	CrawlerMessage map(String message) throws MapException;
}
