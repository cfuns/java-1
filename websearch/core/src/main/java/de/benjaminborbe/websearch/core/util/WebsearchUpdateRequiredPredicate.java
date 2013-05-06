package de.benjaminborbe.websearch.core.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.core.WebsearchConstants;
import de.benjaminborbe.websearch.core.dao.WebsearchConfigurationBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import org.slf4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WebsearchUpdateRequiredPredicate implements Predicate<WebsearchPageBean> {

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private final List<WebsearchConfigurationBean> configurations;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	public WebsearchUpdateRequiredPredicate(final Logger logger, final CalendarUtil calendarUtil, final List<WebsearchConfigurationBean> configurations) {
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
			if (!pageConfigurations.isEmpty() || hasDepth(page)) {

				logger.trace("url " + page.getId() + " is subpage");
				// check age > EXPIRE
				if (isExpired(time, page, pageConfigurations)) {
					logger.trace("url " + page.getId() + " is expired");

					final long delay = getDelay(pageConfigurations);
					try {
						logger.trace("sleep: " + delay);
						Thread.sleep(delay);
					} catch (final InterruptedException e) {
						// nop
					}

					return true;
				} else {
					logger.trace("url " + page.getId() + " is not expired");
				}
			} else {
				logger.trace("url " + page.getId() + " is not subpage");
			}
		} catch (final EntityIteratorException e) {
			logger.warn(e.getClass().getName(), e);
		}
		return false;
	}

	private boolean hasDepth(final WebsearchPageBean page) {
		final Long depth = page.getDepth();
		return depth != null && depth > 0;
	}

	private boolean isExpired(final long time, final WebsearchPageBean page, final List<WebsearchConfigurationBean> pageConfigurations) {
		if (page.getLastVisit() == null) {
			return true;
		}
		int days = WebsearchConstants.DEFAULT_EXPIRE_IN_DAYS;
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
		final List<WebsearchConfigurationBean> result = new ArrayList<>();
		if (page == null) {
			logger.warn("parameter page is null");
			return result;
		}
		final URL url = page.getUrl();
		if (url == null) {
			logger.warn("parameter url is null at page " + page.getId());
			return result;
		}
		for (final WebsearchConfigurationBean configuration : configurations) {
			if (url.toExternalForm().startsWith(configuration.getUrl().toExternalForm())) {
				boolean isExcluded = false;
				for (final String exclude : configuration.getExcludes()) {
					if (url.toExternalForm().contains(exclude)) {
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
		return WebsearchConstants.DEFAULT_DELAY;
	}
}
