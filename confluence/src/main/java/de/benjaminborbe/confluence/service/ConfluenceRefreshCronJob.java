package de.benjaminborbe.confluence.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.apache.xmlrpc.XmlRpcException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.ConfluenceConstants;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorPage;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.html.HtmlUtil;

@Singleton
public class ConfluenceRefreshCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 0 * * * ?"; // ones per hour

	private final Logger logger;

	private final IndexerService indexerService;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final ConfluenceConnector confluenceConnector;

	private final HtmlUtil htmlUtil;

	@Inject
	public ConfluenceRefreshCronJob(
			final Logger logger,
			final IndexerService indexerService,
			final ConfluenceInstanceDao confluenceInstanceDao,
			final ConfluenceConnector confluenceConnector,
			final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.indexerService = indexerService;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.confluenceConnector = confluenceConnector;
		this.htmlUtil = htmlUtil;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		try {
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
				catch (final XmlRpcException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
		catch (final EntityIteratorException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final StorageException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

	private void handle(final ConfluenceInstanceBean confluenceInstance) throws MalformedURLException, XmlRpcException {

		final String confluenceBaseUrl = confluenceInstance.getUrl();
		final String username = confluenceInstance.getUsername();
		final String password = confluenceInstance.getPassword();
		final String token = confluenceConnector.login(confluenceBaseUrl, username, password);
		final Collection<String> spaceKeys = confluenceConnector.getSpaceKeys(confluenceBaseUrl, token);
		for (final String spaceKey : spaceKeys) {
			final Collection<ConfluenceConnectorPage> pages = confluenceConnector.getPages(confluenceBaseUrl, token, spaceKey);
			for (final ConfluenceConnectorPage page : pages) {
				try {
					final String content = confluenceConnector.getRenderedContent(confluenceBaseUrl, token, page.getPageId());
					final URL url = new URL(page.getUrl());
					final String title = page.getTitle();
					indexerService.addToIndex(ConfluenceConstants.INDEX, url, title, htmlUtil.filterHtmlTages(content));
					logger.info("addToIndex " + url.toExternalForm());
				}
				catch (final IndexerServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
	}
}
