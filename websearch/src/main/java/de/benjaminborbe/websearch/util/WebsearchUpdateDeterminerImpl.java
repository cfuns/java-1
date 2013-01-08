package de.benjaminborbe.websearch.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.dao.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.dao.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.dao.WebsearchPageDao;

@Singleton
public class WebsearchUpdateDeterminerImpl implements WebsearchUpdateDeterminer {

	private final Logger logger;

	private final WebsearchPageDao pageDao;

	private final WebsearchConfigurationDao configurationDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public WebsearchUpdateDeterminerImpl(final Logger logger, final WebsearchPageDao pageDao, final WebsearchConfigurationDao configurationDao, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.configurationDao = configurationDao;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public EntityIterator<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException {
		logger.trace("determineExpiredPages");

		// create startpages
		final EntityIterator<WebsearchConfigurationBean> configurationsIterator = configurationDao.getActivatedEntityIterator();
		final List<WebsearchConfigurationBean> configurations = new ArrayList<WebsearchConfigurationBean>();
		while (configurationsIterator.hasNext()) {
			final WebsearchConfigurationBean configuration = configurationsIterator.next();
			configurations.add(configuration);
			pageDao.findOrCreate(configuration.getUrl());
		}

		final EntityIterator<WebsearchPageBean> pages = pageDao.getEntityIterator();
		final Predicate<WebsearchPageBean> predicate = new WebsearchNotExpiredPredicate(logger, calendarUtil, configurations);
		return new EntityIteratorFilter<WebsearchPageBean>(pages, predicate);
	}

}
