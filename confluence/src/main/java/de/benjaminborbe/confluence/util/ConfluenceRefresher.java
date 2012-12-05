package de.benjaminborbe.confluence.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.confluence.ConfluenceConstants;
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

public class ConfluenceRefresher {

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private final Logger logger;

	private final IndexerService indexerService;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final ConfluenceConnector confluenceConnector;

	private final HtmlUtil htmlUtil;

	private final ConfluencePageDao confluencePageDao;

	private final CalendarUtil calendarUtil;

	@Inject
	public ConfluenceRefresher(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final IndexerService indexerService,
			final ConfluenceInstanceDao confluenceInstanceDao,
			final ConfluencePageDao confluencePageDao,
			final ConfluenceConnector confluenceConnector,
			final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.indexerService = indexerService;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.confluencePageDao = confluencePageDao;
		this.confluenceConnector = confluenceConnector;
		this.htmlUtil = htmlUtil;
	}

	private void handle(final ConfluenceInstanceBean confluenceInstanceBean) throws MalformedURLException, XmlRpcException {

		final String indexName;
		if (Boolean.TRUE.equals(confluenceInstanceBean.getShared())) {
			indexName = ConfluenceConstants.INDEX;
		}
		else {
			indexName = ConfluenceConstants.INDEX + "_" + confluenceInstanceBean.getOwner().getId();
		}

		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();
		final String username = confluenceInstanceBean.getUsername();
		final String password = confluenceInstanceBean.getPassword();
		final String token = confluenceConnector.login(confluenceBaseUrl, username, password);
		final Collection<String> spaceKeys = confluenceConnector.getSpaceKeys(confluenceBaseUrl, token);
		for (final String spaceKey : spaceKeys) {
			final Collection<ConfluenceConnectorPage> pages = confluenceConnector.getPages(confluenceBaseUrl, token, spaceKey);
			for (final ConfluenceConnectorPage page : pages) {
				try {
					// check expire
					final ConfluencePageBean pageBean = confluencePageDao.findOrCreate(confluenceInstanceBean.getId(), indexName, page.getPageId());
					if (isExpired(confluenceInstanceBean, pageBean)) {
						final String content = confluenceConnector.getRenderedContent(confluenceBaseUrl, token, page.getPageId());
						final URL url = new URL(page.getUrl());
						final String title = page.getTitle();

						indexerService.addToIndex(indexName, url, title, filterContent(content));

						logger.info("addToIndex " + url.toExternalForm());

						// update lastVisit
						pageBean.setLastVisit(calendarUtil.now());
						confluencePageDao.save(pageBean);
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

	public void refresh() throws EntityIteratorException, StorageException, XmlRpcException {
		logger.info("execute");
		final EntityIterator<ConfluenceInstanceBean> i = confluenceInstanceDao.getEntityIterator();
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

}
