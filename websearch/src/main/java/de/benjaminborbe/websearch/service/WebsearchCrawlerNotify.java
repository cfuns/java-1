package de.benjaminborbe.websearch.service;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.crawler.api.CrawlerResult;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.WebsearchActivator;
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexerService indexerService;

	private final StringUtil stringUtil;

	private final PageDao pageDao;

	private final static String CONTENT_TYPE = "text/html";

	private final HtmlUtil htmlUtil;

	@Inject
	public WebsearchCrawlerNotify(final Logger logger, final IndexerService indexerService, final StringUtil stringUtil, final PageDao pageDao, final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.indexerService = indexerService;
		this.stringUtil = stringUtil;
		this.pageDao = pageDao;
		this.htmlUtil = htmlUtil;
	}

	@Override
	public void notifiy(final CrawlerResult result) {
		logger.trace("notify " + result.getUrl());
		try {
			updateLastVisit(result);
			if (isIndexAble(result)) {
				parseLinks(result);
				addToIndex(result);
			}
		}
		catch (final StorageException e) {
			logger.debug("StorageException", e);
		}
		catch (final IndexerServiceException e) {
			logger.debug("StorageException", e);
		}
	}

	protected boolean isIndexAble(final CrawlerResult result) {
		if (!result.isAvailable()) {
			logger.warn("result not available for url: " + result.getUrl());
			return false;
		}
		final String contentType = result.getContentType();
		if (contentType == null || contentType.indexOf(CONTENT_TYPE) != 0) {
			logger.trace("result has wrong contenttype for url: " + result.getUrl() + " contentType: " + contentType);
			return false;
		}
		return true;
	}

	protected void parseLinks(final CrawlerResult result) {
		final Set<String> links = htmlUtil.parseLinks(result.getContent());
		logger.trace("found " + links.size() + " links");
		for (final String link : links) {
			try {
				final URL url = buildUrl(result.getUrl(), link);
				logger.trace("found page: " + url.toExternalForm());
				pageDao.findOrCreate(url);
			}
			catch (final MalformedURLException e) {
				logger.debug("MalformedURLException", e);
			}
			catch (final StorageException e) {
				logger.debug("StorageException", e);
			}
		}
	}

	protected URL buildUrl(final URL baseUrl, final String link) throws MalformedURLException {
		logger.trace("buildUrl url: " + baseUrl + " link: " + link);
		final String url;
		if (link.startsWith("http://") || link.startsWith("https://")) {
			url = link;
		}
		else if (link.startsWith("/")) {
			final StringWriter sw = new StringWriter();
			sw.append(baseUrl.getProtocol());
			sw.append("://");
			sw.append(baseUrl.getHost());
			if (baseUrl.getPort() != 80 && baseUrl.getPort() != 443 && baseUrl.getPort() != -1) {
				sw.append(":");
				sw.append(String.valueOf(baseUrl.getPort()));
			}
			sw.append(link);
			url = sw.toString();
		}
		else {
			final StringWriter sw = new StringWriter();
			sw.append(baseUrl.getProtocol());
			sw.append("://");
			sw.append(baseUrl.getHost());
			if (baseUrl.getPort() != 80 && baseUrl.getPort() != 443 && baseUrl.getPort() != -1) {
				sw.append(":");
				sw.append(String.valueOf(baseUrl.getPort()));
			}
			sw.append("/");
			final String path = baseUrl.getPath();
			final int pos = path.lastIndexOf("/");
			if (pos != -1) {
				sw.append(path.substring(0, pos));
			}
			sw.append("/");
			sw.append(link);
			url = sw.toString();
		}
		final String result = cleanUpUrl(url);
		logger.trace("result = " + result);
		return new URL(result);
	}

	protected String cleanUpUrl(final String url) {
		final String u = url.replaceAll("//", "/").replaceFirst(":/", "://");
		final int pos = u.indexOf('#');
		if (pos != -1) {
			return u.substring(0, pos);
		}
		else {
			return u;
		}
	}

	protected void addToIndex(final CrawlerResult result) throws IndexerServiceException {
		final Document document = Jsoup.parse(result.getContent());
		for (final Element head : document.getElementsByTag("head")) {
			for (final Element meta : head.getElementsByTag("meta")) {
				// <meta name="robots" content="noindex,nofollow">
				if ("robots".equalsIgnoreCase(meta.attr("name"))) {
					final String content = meta.attr("content");
					final String[] parts = content.split(",");
					for (final String part : parts) {
						if ("noindex".equalsIgnoreCase(part) || "noarchive".equalsIgnoreCase(part)) {
							return;
						}
					}
				}
			}
		}
		logger.trace("add url " + result.getUrl() + " to index");
		indexerService.addToIndex(WebsearchActivator.INDEX, result.getUrl(), extractTitle(result.getContent()), result.getContent());
	}

	protected void updateLastVisit(final CrawlerResult result) throws StorageException {
		final PageBean page = pageDao.findOrCreate(result.getUrl());
		page.setLastVisit(new Date());
		pageDao.save(page);
	}

	protected String extractTitle(final String content) {
		final Document document = Jsoup.parse(content);
		final Elements titleElements = document.getElementsByTag("title");
		for (final Element titleElement : titleElements) {
			return stringUtil.shorten(titleElement.html(), TITLE_MAX_LENGTH);
		}
		return "-";
	}

}
