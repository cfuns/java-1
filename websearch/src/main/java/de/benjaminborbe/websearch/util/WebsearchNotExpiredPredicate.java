package de.benjaminborbe.websearch.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;

import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.configuration.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.page.WebsearchPageBean;

public class WebsearchNotExpiredPredicate implements Predicate<WebsearchPageBean> {

	private static final long DEFAULT_DELAY = 300;

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private final List<WebsearchConfigurationBean> configurations;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	public WebsearchNotExpiredPredicate(final Logger logger, final CalendarUtil calendarUtil, final List<WebsearchConfigurationBean> configurations) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
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
				logger.trace("url " + page.getId() + " is not subpage");
			}
		}
		catch (final EntityIteratorException e) {
			logger.warn(e.getClass().getName(), e);
		}
		return false;
	}

	private boolean isExpired(final long time, final WebsearchPageBean page, final List<WebsearchConfigurationBean> pageConfigurations) {
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
		return time - page.getLastVisit().getTimeInMillis() > days * EXPIRE_DAY;
	}

	private List<WebsearchConfigurationBean> getConfigurationForPage(final WebsearchPageBean page, final Collection<WebsearchConfigurationBean> configurations)
			throws EntityIteratorException {
		final List<WebsearchConfigurationBean> result = new ArrayList<WebsearchConfigurationBean>();
		if (page == null) {
			logger.warn("parameter page is null");
			return result;
		}
		final String url = page.getUrl();
		if (url == null) {
			logger.warn("parameter url is null at page " + page.getId());
			return result;
		}
		for (final WebsearchConfigurationBean configuration : configurations) {
			if (url.startsWith(configuration.getUrl().toExternalForm())) {
				boolean isExcluded = false;
				for (final String exclude : configuration.getExcludes()) {
					if (url.contains(exclude)) {
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

	private long getDelay(final List<WebsearchConfigurationBean> pageConfigurations) {
		for (final WebsearchConfigurationBean configuration : pageConfigurations) {
			if (configuration.getDelay() != null && configuration.getDelay() >= 0) {
				return configuration.getDelay();
			}
		}
		return DEFAULT_DELAY;
	}
}
