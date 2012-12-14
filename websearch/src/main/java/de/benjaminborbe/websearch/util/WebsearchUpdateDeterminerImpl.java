package de.benjaminborbe.websearch.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.page.WebsearchPageBean;
import de.benjaminborbe.websearch.page.WebsearchPageDao;

@Singleton
public class WebsearchUpdateDeterminerImpl implements WebsearchUpdateDeterminer {

	private final class NotExpiredPredicate implements Predicate<WebsearchPageBean> {

		private final List<WebsearchConfigurationBean> configurations;

		private NotExpiredPredicate(final List<WebsearchConfigurationBean> configurations) {
			this.configurations = configurations;
		}

		@Override
		public boolean apply(final WebsearchPageBean page) {
			try {
				final long time = calendarUtil.getTime();
				final List<WebsearchConfigurationBean> pageConfigurations = getConfigurationForPage(page, configurations);
				// handle only pages configuration exists for
				if (!pageConfigurations.isEmpty()) {
					logger.debug("url " + page.getId() + " is subpage");
					// check age > EXPIRE
					if (isExpired(time, page, pageConfigurations)) {
						logger.debug("url " + page.getId() + " is expired");

						final long delay = getDelay(pageConfigurations);
						try {
							Thread.sleep(delay);
						}
						catch (final InterruptedException e) {
						}

						return true;
					}
					else {
						logger.debug("url " + page.getId() + " is not expired");
					}
				}
				else {
					logger.debug("url " + page.getId() + " is not subpage");
				}
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
			return false;
		}
	}

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private static final long DEFAULT_DELAY = 300;

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

	public long getDelay(final List<WebsearchConfigurationBean> pageConfigurations) {
		for (final WebsearchConfigurationBean configuration : pageConfigurations) {
			if (configuration.getDelay() != null && configuration.getDelay() >= 0) {
				return configuration.getDelay();
			}
		}
		return DEFAULT_DELAY;
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
		final Predicate<WebsearchPageBean> predicate = new NotExpiredPredicate(configurations);
		return new EntityIteratorFilter<WebsearchPageBean>(pages, predicate);
	}

	protected boolean isExpired(final long time, final WebsearchPageBean page, final List<WebsearchConfigurationBean> pageConfigurations) {
		if (page.getLastVisit() == null) {
			return true;
		}
		int days = 7;
		for (final WebsearchConfigurationBean websearchConfigurationBean : pageConfigurations) {
			final Integer expire = websearchConfigurationBean.getExpire();
			if (expire != null && expire > 0) {
				days = expire;
			}
		}
		return time - page.getLastVisit().getTime() > days * EXPIRE_DAY;
	}

	protected boolean isSubPage(final WebsearchPageBean page, final Collection<WebsearchConfigurationBean> configurations) throws EntityIteratorException {
		return !getConfigurationForPage(page, configurations).isEmpty();
	}

	protected List<WebsearchConfigurationBean> getConfigurationForPage(final WebsearchPageBean page, final Collection<WebsearchConfigurationBean> configurations)
			throws EntityIteratorException {
		final List<WebsearchConfigurationBean> result = new ArrayList<WebsearchConfigurationBean>();
		if (page == null) {
			logger.warn("parameter page is null");
			return result;
		}
		final URL url = page.getUrl();
		if (url == null) {
			logger.warn("parameter url is null at page " + page.getId());
			return result;
		}
		final String urlString = url.toExternalForm();
		for (final WebsearchConfigurationBean configuration : configurations) {
			if (urlString.startsWith(configuration.getUrl().toExternalForm())) {
				boolean isExcluded = false;
				for (final String exclude : configuration.getExcludes()) {
					if (urlString.contains(exclude)) {
						isExcluded = true;
					}
				}
				if (isExcluded == false) {
					result.add(configuration);
				}
			}
		}
		return result;
	}
}
