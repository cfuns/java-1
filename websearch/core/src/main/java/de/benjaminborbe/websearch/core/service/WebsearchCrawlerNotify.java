package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.crawler.api.CrawlerNotifier;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpUtil;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.core.WebsearchConstants;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexService indexerService;

	private final StringUtil stringUtil;

	private final WebsearchPageDao pageDao;

	private final HtmlUtil htmlUtil;

	private final HttpUtil httpUtil;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public WebsearchCrawlerNotify(
		final Logger logger,
		final HttpUtil httpUtil,
		final ParseUtil parseUtil,
		final CalendarUtil calendarUtil,
		final IndexService indexerService,
		final StringUtil stringUtil,
		final WebsearchPageDao pageDao,
		final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.httpUtil = httpUtil;
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
		this.indexerService = indexerService;
		this.stringUtil = stringUtil;
		this.pageDao = pageDao;
		this.htmlUtil = htmlUtil;
	}

	@Override
	public void notifiy(final HttpResponse result) {
		try {
			logger.trace("notify " + result.getUrl());
			updateLastVisit(result);
			if (isHtmlPage(result)) {
				parseLinks(result);
				addToIndex(result);
			}
		} catch (final StorageException | ParseException | IndexerServiceException e) {
			logger.debug("StorageException", e);
		}
	}

	protected boolean isHtmlPage(final HttpResponse result) {
		if (!httpUtil.isAvailable(result)) {
			logger.warn("result not available for url: " + result.getUrl() + " returnCode: " + result.getReturnCode());
			return false;
		}
		if (!httpUtil.isHtml(result.getHeader())) {
			logger.trace("result has wrong contenttype for url: " + result.getUrl() + " contentType: " + httpUtil.getContentType(result.getHeader()));
			return false;
		}
		return true;
	}

	protected void parseLinks(final HttpResponse result) {
		final Collection<String> links = htmlUtil.parseLinks(httpUtil.getContent(result));
		logger.trace("found " + links.size() + " links");
		for (final String link : links) {
			try {
				if (isValidLink(link)) {
					final URL url = buildUrl(result.getUrl(), link.trim());
					logger.trace("found page: " + url.toExternalForm());
					pageDao.findOrCreate(url);
				}
			} catch (MalformedURLException | StorageException | ParseException e) {
				logger.debug(e.getClass().getName(), e);
			}
		}
	}

	private boolean isValidLink(final String link) {
		final String linkLower = link.toLowerCase().trim();
		return !linkLower.startsWith("javascript:") && !linkLower.startsWith("feed:");
	}

	protected URL buildUrl(final URL baseUrl, final String link) throws MalformedURLException, ParseException {
		logger.trace("buildUrl url: " + baseUrl + " link: " + link);
		final String url;
		if (link.startsWith("http://") || link.startsWith("https://")) {
			url = link;
		} else if (link.startsWith("/")) {
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
		} else {
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
		return parseUtil.parseURL(result);
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
			} else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost);
			}
		} else {
			if (posHost == -1) {
				host = u.substring(0, posHash);
				uri = "";
			} else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost, posHash);
			}
		}

		final String[] parts = uri.split("/");
		final List<String> p = new ArrayList<>();
		for (int i = parts.length - 1; i >= 0; i--) {
			final String part = parts[i];
			if (part != null && !part.isEmpty()) {
				if ("..".equals(part)) {
					i--;
				} else {
					p.add(part);
				}
			}
		}

		p.add(host);
		Collections.reverse(p);

		return StringUtils.join(p, "/");
	}

	private void addToIndex(final HttpResponse result) throws IndexerServiceException, ParseException {
		final String html = httpUtil.getContent(result);
		final Document document = Jsoup.parse(html);
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
		indexerService.addToIndex(WebsearchConstants.INDEX, result.getUrl(), extractTitle(html), htmlUtil.filterHtmlTages(html), null);
	}

	private void updateLastVisit(final HttpResponse result) throws StorageException {
		final WebsearchPageBean page = pageDao.findOrCreate(result.getUrl());
		page.setLastVisit(calendarUtil.now());
		pageDao.save(page);
	}

	protected String extractTitle(final String content) throws ParseException {
		final Document document = Jsoup.parse(content);
		final Elements titleElements = document.getElementsByTag("title");
		for (final Element titleElement : titleElements) {
			return stringUtil.shorten(htmlUtil.filterHtmlTages(titleElement.html()), TITLE_MAX_LENGTH);
		}
		return "-";
	}
}
