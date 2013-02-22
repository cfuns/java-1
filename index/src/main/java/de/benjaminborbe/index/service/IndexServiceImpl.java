package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.index.util.IndexServiceFactory;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class IndexServiceImpl implements IndexService {

	private final IndexServiceFactory indexServiceFactory;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	@Inject
	public IndexServiceImpl(final Logger logger, final IndexServiceFactory indexServiceFactory, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.indexServiceFactory = indexServiceFactory;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content, final Calendar date) throws IndexerServiceException {
		logger.debug("addToIndex - index: " + index + " url: " + url + " date: " + calendarUtil.toDateTimeString(date) + " title: " + title + " content: " + content);
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			indexService.addToIndex(index, url, title, content, date);
		}
	}

	@Override
	public void clear(final String index) throws IndexerServiceException {
		logger.debug("clear - index: " + index);
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			indexService.clear(index);
		}
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		logger.debug("search - index: " + index + " searchQuery: " + searchQuery);
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			return indexService.search(index, searchQuery, limit);
		}
		throw new IndexerServiceException("at least one index must be enabled");
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws IndexerServiceException {
		logger.debug("removeFromIndex - index: " + index + " url: " + url);
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			indexService.removeFromIndex(index, url);
		}
	}

}
