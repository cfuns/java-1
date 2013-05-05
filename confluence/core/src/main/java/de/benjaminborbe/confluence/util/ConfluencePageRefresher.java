package de.benjaminborbe.confluence.util;

import de.benjaminborbe.confluence.api.ConfluencePageIdentifier;
import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorLabel;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorPage;
import de.benjaminborbe.confluence.connector.ConfluenceConnectorSession;
import de.benjaminborbe.confluence.connector.ConfluenceXmlRpcClientException;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.confluence.dao.ConfluencePageBean;
import de.benjaminborbe.confluence.dao.ConfluencePageDao;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConfluencePageRefresher {

	private final Logger logger;

	private final ConfluenceIndexUtil confluenceIndexUtil;

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	private final ConfluenceConnector confluenceConnector;

	private final IndexService indexerService;

	private final ConfluencePageDao confluencePageDao;

	private final HtmlUtil htmlUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	@Inject
	public ConfluencePageRefresher(
		final Logger logger,
		final ConfluenceIndexUtil confluenceIndexUtil,
		final CalendarUtil calendarUtil,
		final ParseUtil parseUtil,
		final ConfluenceConnector confluenceConnector,
		final IndexService indexerService,
		final ConfluencePageDao confluencePageDao,
		final HtmlUtil htmlUtil,
		final TimeZoneUtil timeZoneUtil,
		final ConfluenceInstanceDao confluenceInstanceDao
	) {
		this.logger = logger;
		this.confluenceIndexUtil = confluenceIndexUtil;
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
		this.confluenceConnector = confluenceConnector;
		this.indexerService = indexerService;
		this.confluencePageDao = confluencePageDao;
		this.htmlUtil = htmlUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.confluenceInstanceDao = confluenceInstanceDao;
	}

	public void refreshPage(
		final ConfluenceConnectorSession confluenceConnectorSession,
		final ConfluenceInstanceBean confluenceInstanceBean,
		final ConfluencePageBean confluencePageBean,
		final ConfluenceConnectorPage confluenceConnectorPage
	) throws StorageException, ParseException, MalformedURLException, ConfluenceXmlRpcClientException, IndexerServiceException {
		logger.debug("update confluenceConnectorPage " + confluenceConnectorPage.getTitle());
		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();
		final String content = confluenceConnector.getRenderedContent(confluenceConnectorSession, confluenceConnectorPage.getPageId());
		logger.trace("confluenceConnectorPage-content: " + content);
		final URL url = parseUtil.parseURL(confluenceConnectorPage.getUrl());
		final String title = confluenceConnectorPage.getTitle();
		final String indexName = confluenceIndexUtil.getIndex(confluenceInstanceBean);
		final Calendar pageModified = toCalendar(confluenceConnectorPage.getModified());

		List<ConfluenceConnectorLabel> labels = confluenceConnector.getLabels(confluenceConnectorSession, confluenceConnectorPage.getPageId());

		indexerService.addToIndex(indexName, url, title, prepareContent(content, labels), calendarUtil.getCalendar(confluenceConnectorPage.getModified()));

		logger.debug("addToIndex " + url.toExternalForm());

		// update lastVisit
		confluencePageBean.setLastVisit(calendarUtil.now());
		confluencePageBean.setLastModified(pageModified);
		confluencePageBean.setPageId(confluenceConnectorPage.getPageId());
		confluencePageBean.setOwner(confluenceInstanceBean.getOwner());
		confluencePageBean.setInstanceId(confluenceInstanceBean.getId());
		confluencePageBean.setUrl(url);
		confluencePageDao.save(confluencePageBean);
	}

	private String prepareContent(final String orgContent, final List<ConfluenceConnectorLabel> labels) throws ParseException {
		logger.trace("content: " + orgContent);
		final String filteredContent = htmlUtil.filterHtmlTages(orgContent);
		logger.trace("filteredContent: " + filteredContent);

		StringBuilder sb = new StringBuilder();
		sb.append(filteredContent);
		if (labels != null) {
			for (ConfluenceConnectorLabel label : labels) {
				sb.append(' ');
				sb.append(label.getLabel());
			}
		}
		final String result = sb.toString();
		logger.trace("filteredContentWithLabels: " + result);
		return result;
	}

	private Calendar toCalendar(final Date date) {
		return calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), date, null);
	}

	public void refreshPage(final ConfluencePageIdentifier confluencePageIdentifier) throws StorageException, ParseException, ConfluenceXmlRpcClientException, MalformedURLException, IndexerServiceException {
		final ConfluencePageBean confluencePageBean = confluencePageDao.load(confluencePageIdentifier);
		final ConfluenceInstanceBean confluenceInstanceBean = confluenceInstanceDao.load(confluencePageBean.getInstanceId());
		final String confluenceBaseUrl = confluenceInstanceBean.getUrl();
		final ConfluenceConnectorSession token = confluenceConnector.login(confluenceInstanceBean);
		final ConfluenceConnectorPage confluenceConnectorPage = confluenceConnector.getPage(token, confluencePageBean.getPageId());
		refreshPage(token, confluenceInstanceBean, confluencePageBean, confluenceConnectorPage);
	}
}
