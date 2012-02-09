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
		logger.debug("notify");
		addToIndex(result);
		updateLastVisit(result);
		parseLinks(result);
	}

	protected void parseLinks(final CrawlerResult result) {
		final Set<String> links = htmlUtil.parseLinks(result.getContent());
		logger.debug("found " + links.size() + " links");
		for (final String link : links) {
			try {
				final URL url = buildUrl(result.getUrl(), link);
				logger.debug("found page: " + url.toExternalForm());
				pageDao.findOrCreate(url);
			}
			catch (final MalformedURLException e) {
				logger.debug("MalformedURLException", e);
			}
		}
	}

	protected URL buildUrl(final URL baseUrl, final String link) throws MalformedURLException {
		logger.debug("buildUrl url: " + (baseUrl != null ? baseUrl.toExternalForm() : "null") + " link: " + link);
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
			url = baseUrl.toExternalForm() + link;
		}
		final String result = cleanUpUrl(url);
		logger.debug("result = " + result);
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

	protected void addToIndex(final CrawlerResult result) {
		indexerService.addToIndex(WebsearchActivator.INDEX, result.getUrl(), extractTitle(result.getContent()), result.getContent());
	}

	protected void updateLastVisit(final CrawlerResult result) {
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
