package de.benjaminborbe.websearch.util;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.configuration.ConfigurationBean;
import de.benjaminborbe.websearch.configuration.ConfigurationDao;
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class UpdateDeterminerImpl implements UpdateDeterminer {

	// 1 day
	private static final long EXPIRE = 24l * 60l * 60l * 1000l;

	private final Logger logger;

	private final PageDao pageDao;

	private final ConfigurationDao configurationDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public UpdateDeterminerImpl(final Logger logger, final PageDao pageDao, final ConfigurationDao configurationDao, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.pageDao = pageDao;
		this.configurationDao = configurationDao;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Collection<PageBean> determineExpiredPages() throws StorageException {
		logger.trace("determineExpiredPages");
		final long time = calendarUtil.getTime();
		final Collection<ConfigurationBean> configurations = configurationDao.getAll();
		final Set<PageBean> result = new HashSet<PageBean>();
		final Collection<PageBean> pages = pageDao.getAll();
		logger.trace("found " + pages.size() + " pages to analyse");
		for (final PageBean page : pages) {
			// handle only pages configuration exists for
			if (isSubPage(page, configurations)) {
				logger.trace("url " + page.getId() + " is subpage");
				// check age > EXPIRE
				if (isExpired(time, page)) {
					logger.trace("url " + page.getId() + " is expired");
					result.add(page);
				}
				else {
					logger.trace("url " + page.getId() + " is not expired");
				}
			}
			else {
				logger.trace("url " + page.getId() + " is not subpage");
			}
		}
		logger.trace("determineExpiredPages total: " + pages.size() + " result: " + result.size());
		return result;
	}

	private boolean isExpired(final long time, final PageBean page) {
		return page.getLastVisit() == null || (time - page.getLastVisit().getTime() > EXPIRE);
	}

	protected boolean isSubPage(final PageBean page, final Collection<ConfigurationBean> configurations) {
		if (page == null) {
			throw new NullPointerException("parameter page is null");
		}
		final URL url = page.getUrl();
		if (url == null) {
			throw new NullPointerException("parameter url is null at page " + page.getId());
		}
		final String urlString = url.toExternalForm();
		for (final ConfigurationBean configuration : configurations) {
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
