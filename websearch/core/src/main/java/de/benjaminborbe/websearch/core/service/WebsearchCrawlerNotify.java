package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private final Logger logger;

	private final WebsearchPageDao pageDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public WebsearchCrawlerNotify(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final WebsearchPageDao pageDao
	) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.pageDao = pageDao;
	}

	@Override
	public void notifiy(final HttpResponse result) {
		try {
			logger.trace("notify " + result.getUrl());
			updateLastVisit(result);
		} catch (final StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private void updateLastVisit(final HttpResponse result) throws StorageException {
		final WebsearchPageBean page = pageDao.findOrCreate(result.getUrl());
		page.setLastVisit(calendarUtil.now());
		page.setHeader(result.getHeader());
		page.setContent(result.getContent());
		page.setReturnCode(result.getReturnCode());
		pageDao.save(page);
	}
}