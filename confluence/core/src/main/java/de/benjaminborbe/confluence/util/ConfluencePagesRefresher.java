package de.benjaminborbe.confluence.util;

import de.benjaminborbe.confluence.config.ConfluenceConfig;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorPage;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorPageSummary;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorSession;
import de.benjaminborbe.confluence.connector.ConfluenceXmlRpcClientException;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.confluence.dao.ConfluencePageBean;
import de.benjaminborbe.confluence.dao.ConfluencePageDao;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.list.ListUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ConfluencePagesRefresher {

	private final class RefreshPages implements Runnable {

		@Override
		public void run() {
			try {
				logger.debug("refresh started");
				final EntityIterator<ConfluenceInstanceBean> i = confluenceInstanceDao.getActivatedEntityIterator();
				int counter = 0;
				while (i.hasNext()) {
					counter++;
					final ConfluenceInstanceBean confluenceInstance = i.next();
					try {
						handle(confluenceInstance);
					} catch (final Exception e) {
						logger.warn(e.getClass().getName(), e);
					}
				}
				logger.debug("refresh of " + counter + " confluenceInstances finished");
			} catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			} catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			} finally {
				logger.debug("refresh finished");
			}
		}
	}

	private static final long DEFAULT_DELAY = 300;

	private final Logger logger;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final ConfluenceConnector confluenceConnector;

	private final ConfluencePageDao confluencePageDao;

	private final CalendarUtil calendarUtil;

	private final ConfluenceIndexUtil confluenceIndexUtil;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final TimeZoneUtil timeZoneUtil;

	private final ConfluenceConfig confluenceConfig;

	private final ConfluencePageExpiredCalculator confluencePageExpiredCalculator;

	private final ConfluencePageRefresher confluencePageRefresher;

	private final ListUtil listUtil;

	@Inject
	public ConfluencePagesRefresher(
		final Logger logger,
		final RunOnlyOnceATime runOnlyOnceATime,
		final CalendarUtil calendarUtil,
		final ConfluenceInstanceDao confluenceInstanceDao,
		final ConfluencePageDao confluencePageDao,
		final ConfluenceConnector confluenceConnector,
		final ListUtil listUtil,
		final TimeZoneUtil timeZoneUtil,
		final ConfluenceConfig confluenceConfig,
		final ConfluenceIndexUtil confluenceIndexUtil,
		final ConfluencePageExpiredCalculator confluencePageExpiredCalculator,
		final ConfluencePageRefresher confluencePageRefresher
	) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.calendarUtil = calendarUtil;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.confluencePageDao = confluencePageDao;
		this.confluenceConnector = confluenceConnector;
		this.listUtil = listUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.confluenceConfig = confluenceConfig;
		this.confluenceIndexUtil = confluenceIndexUtil;
		this.confluencePageExpiredCalculator = confluencePageExpiredCalculator;
		this.confluencePageRefresher = confluencePageRefresher;
	}

	private void handle(final ConfluenceInstanceBean confluenceInstanceBean) throws MalformedURLException, ConfluenceXmlRpcClientException, ParseException {
		final long delay = getDelay(confluenceInstanceBean);
		final String indexName = confluenceIndexUtil.getIndex(confluenceInstanceBean);
		int counter = 0;
		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();

		final ConfluenceConnectorSession token = confluenceConnector.login(confluenceInstanceBean);

		final List<String> spaceKeys = listUtil.toList(confluenceConnector.getSpaceKeys(token));

		Collections.shuffle(spaceKeys);

		logger.debug("found " + spaceKeys.size() + " spaces in " + confluenceBaseUrl);
		for (final String spaceKey : spaceKeys) {
			logger.debug("process space " + spaceKey);
			final List<ConfluenceConnectorPageSummary> pageSummaries = listUtil.toList(confluenceConnector.getPageSummaries(token, spaceKey));

			Collections.shuffle(pageSummaries);

			logger.debug("found " + pageSummaries.size() + " pages in space " + spaceKey);
			for (final ConfluenceConnectorPageSummary pageSummary : pageSummaries) {

				// Throttle crawling
				try {
					logger.debug("sleep: " + delay);
					Thread.sleep(delay);
				} catch (final InterruptedException e) {
					// nop
				}

				if (confluenceConfig.getRefreshLimit() != null && confluenceConfig.getRefreshLimit() >= 0 && counter >= confluenceConfig.getRefreshLimit()) {
					logger.debug("refresh limit reached => exit refresh");
					return;
				}

				final ConfluenceConnectorPage page = confluenceConnector.getPage(token, pageSummary);
				final Calendar pageModified = toCalendar(page.getModified());
				logger.debug("process page " + page.getTitle() + " lastmodified: " + calendarUtil.toDateTimeString(pageModified));
				try {
					// check expire
					final ConfluencePageBean pageBean = confluencePageDao.findOrCreate(confluenceInstanceBean.getId(), indexName, page.getPageId());
					if (isExpired(confluenceInstanceBean, pageBean, page)) {

						counter++;
						logger.debug("refresh-counter: " + counter);
						confluencePageRefresher.refreshPage(token, confluenceInstanceBean, pageBean, page);
					} else {
						logger.debug("skip page " + page.getTitle());
					}
				} catch (final IndexerServiceException e) {
					logger.warn(e.getClass().getName(), e);
				} catch (final StorageException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
	}

	private long getDelay(final ConfluenceInstanceBean confluenceInstanceBean) {
		if (confluenceInstanceBean.getDelay() != null && confluenceInstanceBean.getDelay() >= 0) {
			return confluenceInstanceBean.getDelay();
		}
		return DEFAULT_DELAY;
	}

	private boolean isExpired(final ConfluenceInstanceBean confluenceInstanceBean, final ConfluencePageBean pageBean, final ConfluenceConnectorPage page) {
		final Calendar lastVisit = pageBean.getLastVisit();
		final Calendar lastModified = pageBean.getLastModified();
		final Calendar pageModified = toCalendar(page.getModified());
		Integer maxLastVisitInDays = confluenceInstanceBean.getExpire();
		if (maxLastVisitInDays == null) {
			maxLastVisitInDays = 7;
		}
		return confluencePageExpiredCalculator.isExpired(lastVisit, maxLastVisitInDays, lastModified, pageModified);
	}

	private Calendar toCalendar(final Date date) {
		return calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), date, null);
	}

	public boolean refresh() {
		logger.debug("confluence refresh - started");
		if (runOnlyOnceATime.run(new RefreshPages())) {
			logger.debug("confluence refresh - finished");
			return true;
		} else {
			logger.debug("confluence refresh - skipped");
			return false;
		}
	}
}
