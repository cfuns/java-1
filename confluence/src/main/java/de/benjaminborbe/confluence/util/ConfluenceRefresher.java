package de.benjaminborbe.confluence.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorPage;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.confluence.dao.ConfluencePageBean;
import de.benjaminborbe.confluence.dao.ConfluencePageDao;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class ConfluenceRefresher {

	private final class RefreshRunnable implements Runnable {

		@Override
		public void run() {
			try {
				logger.info("refresh started");
				final EntityIterator<ConfluenceInstanceBean> i = confluenceInstanceDao.getActivatedEntityIterator();
				while (i.hasNext()) {
					try {
						final ConfluenceInstanceBean confluenceInstance = i.next();
						handle(confluenceInstance);
					}
					catch (final MalformedURLException e) {
						logger.warn(e.getClass().getName(), e);
					}
				}
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final XmlRpcException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final EntityIteratorException e) {
				logger.warn(e.getClass().getName(), e);
			}
			finally {
				logger.info("refresh finished");
			}
		}
	}

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private static final long DEFAULT_DELAY = 300;

	private final Logger logger;

	private final IndexerService indexerService;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final ConfluenceConnector confluenceConnector;

	private final HtmlUtil htmlUtil;

	private final ConfluencePageDao confluencePageDao;

	private final CalendarUtil calendarUtil;

	private final ConfluenceIndexUtil confluenceIndexUtil;

	private final RunOnlyOnceATime runOnlyOnceATime;

	@Inject
	public ConfluenceRefresher(
			final Logger logger,
			final RunOnlyOnceATime runOnlyOnceATime,
			final CalendarUtil calendarUtil,
			final IndexerService indexerService,
			final ConfluenceInstanceDao confluenceInstanceDao,
			final ConfluencePageDao confluencePageDao,
			final ConfluenceConnector confluenceConnector,
			final HtmlUtil htmlUtil,
			final ConfluenceIndexUtil confluenceIndexUtil) {
		this.logger = logger;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.calendarUtil = calendarUtil;
		this.indexerService = indexerService;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.confluencePageDao = confluencePageDao;
		this.confluenceConnector = confluenceConnector;
		this.htmlUtil = htmlUtil;
		this.confluenceIndexUtil = confluenceIndexUtil;
	}

	private void handle(final ConfluenceInstanceBean confluenceInstanceBean) throws MalformedURLException, XmlRpcException {
		final long delay = getDelay(confluenceInstanceBean);
		final String indexName;
		if (Boolean.TRUE.equals(confluenceInstanceBean.getShared())) {
			indexName = confluenceIndexUtil.indexShared();
		}
		else {
			indexName = confluenceIndexUtil.indexPrivate(confluenceInstanceBean.getOwner());
		}

		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();
		final String username = confluenceInstanceBean.getUsername();
		final String password = confluenceInstanceBean.getPassword();
		final String token = confluenceConnector.login(confluenceBaseUrl, username, password);
		final Collection<String> spaceKeys = confluenceConnector.getSpaceKeys(confluenceBaseUrl, token);
		logger.debug("found " + spaceKeys.size() + " spaces in " + confluenceBaseUrl);
		for (final String spaceKey : spaceKeys) {
			logger.debug("process space " + spaceKey);
			final Collection<ConfluenceConnectorPage> pages = confluenceConnector.getPages(confluenceBaseUrl, token, spaceKey);
			logger.debug("found " + pages.size() + " pages in space " + spaceKey);
			for (final ConfluenceConnectorPage page : pages) {
				logger.debug("process page " + page.getTitle());
				try {
					// check expire
					final ConfluencePageBean pageBean = confluencePageDao.findOrCreate(confluenceInstanceBean.getId(), indexName, page.getPageId());
					if (isExpired(confluenceInstanceBean, pageBean)) {
						logger.debug("update page " + page.getTitle());
						final String content = confluenceConnector.getRenderedContent(confluenceBaseUrl, token, page.getPageId());
						final URL url = new URL(page.getUrl());
						final String title = page.getTitle();

						indexerService.addToIndex(indexName, url, title, filterContent(content));

						logger.info("addToIndex " + url.toExternalForm());

						// update lastVisit
						pageBean.setLastVisit(calendarUtil.now());
						pageBean.setPageId(page.getPageId());
						pageBean.setOwner(confluenceInstanceBean.getOwner());
						pageBean.setInstanceId(confluenceInstanceBean.getId());
						confluencePageDao.save(pageBean);

						// Throttle crawling
						try {
							Thread.sleep(delay);
						}
						catch (final InterruptedException e) {
						}
					}
					else {
						logger.debug("skip page " + page.getTitle());
					}
				}
				catch (final IndexerServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
				catch (final StorageException e) {
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

	private boolean isExpired(final ConfluenceInstanceBean confluenceInstanceBean, final ConfluencePageBean pageBean) {
		final Calendar lastVisit = pageBean.getLastVisit();
		if (lastVisit == null) {
			return true;
		}
		Integer expire = confluenceInstanceBean.getExpire();
		if (expire == null) {
			expire = 7;
		}
		return calendarUtil.getTime() - lastVisit.getTimeInMillis() > expire * EXPIRE_DAY;
	}

	private String filterContent(final String orgContent) {
		final String filteredContent = htmlUtil.filterHtmlTages(orgContent);
		logger.trace("filterContent - orgContent: " + orgContent + " filteredContent: " + filteredContent);
		return filteredContent;
	}

	public boolean refresh() {
		return runOnlyOnceATime.run(new RefreshRunnable());
	}
}
