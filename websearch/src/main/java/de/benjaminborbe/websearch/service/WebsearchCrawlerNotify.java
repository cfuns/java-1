package de.benjaminborbe.websearch.service;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import de.benjaminborbe.websearch.WebsearchConstants;
import de.benjaminborbe.websearch.page.WebsearchPageBean;
import de.benjaminborbe.websearch.page.WebsearchPageDao;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexerService indexerService;

	private final StringUtil stringUtil;

	private final WebsearchPageDao pageDao;

	private final static String CONTENT_TYPE = "text/html";

	private final HtmlUtil htmlUtil;

	@Inject
	public WebsearchCrawlerNotify(final Logger logger, final IndexerService indexerService, final StringUtil stringUtil, final WebsearchPageDao pageDao, final HtmlUtil htmlUtil) {
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
		final Collection<String> links = htmlUtil.parseLinks(result.getContent());
		logger.trace("found " + links.size() + " links");
		for (final String link : links) {
			final String linkLower = link.toLowerCase().trim();
			try {
				if (!linkLower.startsWith("javascript:")) {
					final URL url = buildUrl(result.getUrl(), link);
					logger.trace("found page: " + url.toExternalForm());
					pageDao.findOrCreate(url);
				}
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

		final int posDoubleDash = u.indexOf("://");
		final int posHost = u.indexOf("/", posDoubleDash + 3);
		final int posHash = u.indexOf('#', posDoubleDash + 3);

		final String host;
		final String uri;
		if (posHash == -1) {
			if (posHost == -1) {
				host = u;
				uri = "";
			}
			else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost);
			}
		}
		else {
			if (posHost == -1) {
				host = u.substring(0, posHash);
				uri = "";
			}
			else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost, posHash);
			}
		}

		final String[] parts = uri.split("/");
		final List<String> p = new ArrayList<String>();
		for (int i = parts.length - 1; i >= 0; i--) {
			final String part = parts[i];
			if (part != null && !part.isEmpty()) {
				if ("..".equals(part)) {
					i--;
				}
				else {
					p.add(part);
				}
			}
		}

		p.add(host);
		Collections.reverse(p);

		return StringUtils.join(p, "/");
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
		indexerService.addToIndex(WebsearchConstants.INDEX, result.getUrl(), extractTitle(result.getContent()), htmlUtil.filterHtmlTages(result.getContent()));
	}

	protected void updateLastVisit(final CrawlerResult result) throws StorageException {
		final WebsearchPageBean page = pageDao.findOrCreate(result.getUrl());
		page.setLastVisit(new Date());
		pageDao.save(page);
	}

	protected String extractTitle(final String content) {
		final Document document = Jsoup.parse(content);
		final Elements titleElements = document.getElementsByTag("title");
		for (final Element titleElement : titleElements) {
			return stringUtil.shorten(htmlUtil.filterHtmlTages(titleElement.html()), TITLE_MAX_LENGTH);
		}
		return "-";
	}
}
