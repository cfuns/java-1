package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
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
	public void notifiy(final CrawlerNotifierResult httpResponse) {
		try {
			logger.trace("notify " + httpResponse.getUrl());
			updateLastVisit(httpResponse);
		} catch (final StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private void updateLastVisit(final CrawlerNotifierResult httpResponse) throws StorageException {
		final WebsearchPageBean page = pageDao.findOrCreate(httpResponse.getUrl(), httpResponse.getDepth(), httpResponse.getTimeout());
		page.setLastVisit(calendarUtil.now());
		page.setHeader(httpResponse.getHeader());
		page.setContent(httpResponse.getContent());
		page.setReturnCode(httpResponse.getReturnCode());
		page.setDuration(httpResponse.getDuration());
		pageDao.save(page);
	}
}