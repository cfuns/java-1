package de.benjaminborbe.websearch.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationDao;
import de.benjaminborbe.websearch.page.WebsearchPageBean;
import de.benjaminborbe.websearch.page.WebsearchPageDao;

@Singleton
public class WebsearchUpdateDeterminerImpl implements WebsearchUpdateDeterminer {

	// 1 day
	private static final long EXPIRE = 24l * 60l * 60l * 1000l;

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
	public Collection<WebsearchPageBean> determineExpiredPages() throws StorageException, EntityIteratorException {
		logger.trace("determineExpiredPages");
		final long time = calendarUtil.getTime();
		final EntityIterator<WebsearchConfigurationBean> configurationsIterator = configurationDao.getEntityIterator();
		final List<WebsearchConfigurationBean> configurations = new ArrayList<WebsearchConfigurationBean>();
		while (configurationsIterator.hasNext()) {
			configurations.add(configurationsIterator.next());
		}

		final Set<WebsearchPageBean> result = new HashSet<WebsearchPageBean>();
		final EntityIterator<WebsearchPageBean> pages = pageDao.getEntityIterator();
		while (pages.hasNext()) {
			final WebsearchPageBean page = pages.next();
			// handle only pages configuration exists for
			if (isSubPage(page, configurations)) {
				logger.debug("url " + page.getId() + " is subpage");
				// check age > EXPIRE
				if (isExpired(time, page)) {
					logger.debug("url " + page.getId() + " is expired");
					result.add(page);
				}
				else {
					logger.debug("url " + page.getId() + " is not expired");
				}
			}
			else {
				logger.debug("url " + page.getId() + " is not subpage");
			}
		}
		return result;
	}

	private boolean isExpired(final long time, final WebsearchPageBean page) {
		return page.getLastVisit() == null || (time - page.getLastVisit().getTime() > EXPIRE);
	}

	protected boolean isSubPage(final WebsearchPageBean page, final Collection<WebsearchConfigurationBean> configurations) throws EntityIteratorException {
		if (page == null) {
			throw new NullPointerException("parameter page is null");
		}
		final URL url = page.getUrl();
		if (url == null) {
			throw new NullPointerException("parameter url is null at page " + page.getId());
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
					return true;
				}
			}
		}
		return false;
	}
}
