package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class WebsearchUpdateDeterminerImpl implements WebsearchUpdateDeterminer {

	private final Logger logger;

	private final WebsearchPageDao pageDao;

	private final WebsearchConfigurationDao configurationDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public WebsearchUpdateDeterminerImpl(
		final Logger logger,
		final WebsearchPageDao pageDao,
		final WebsearchConfigurationDao configurationDao,
		final CalendarUtil calendarUtil
	) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.configurationDao = configurationDao;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public EntityIterator<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException {
		logger.trace("determineExpiredPages");
		final List<WebsearchConfigurationBean> configurations = createStartPages();
		final EntityIterator<WebsearchPageBean> pages = pageDao.getEntityIterator();
		final WebsearchUpdateRequiredPredicate websearchExpiredPredicate = new WebsearchUpdateRequiredPredicate(logger, calendarUtil, configurations);
		return new EntityIteratorFilter<>(pages, websearchExpiredPredicate);
	}

	private List<WebsearchConfigurationBean> createStartPages() throws StorageException, EntityIteratorException {
		final EntityIterator<WebsearchConfigurationBean> configurationsIterator = configurationDao.getActivatedEntityIterator();
		final List<WebsearchConfigurationBean> configurations = new ArrayList<>();
		while (configurationsIterator.hasNext()) {
			final WebsearchConfigurationBean configuration = configurationsIterator.next();
			configurations.add(configuration);
			pageDao.findOrCreate(configuration.getUrl(), null, null);
		}
		return configurations;
	}

}
