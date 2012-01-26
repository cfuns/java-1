package de.benjaminborbe.websearch.service;

import java.net.MalformedURLException;
import java.net.URL;

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
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.WebsearchActivator;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexerService indexerService;

	private final StringUtil stringUtil;

	@Inject
	public WebsearchCrawlerNotify(final Logger logger, final IndexerService indexerService, final StringUtil stringUtil) {
		this.logger = logger;
		this.indexerService = indexerService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void notifiy(final CrawlerResult result) {
		logger.debug("notify");
		try {
			indexerService.addToIndex(WebsearchActivator.INDEX, new URL(result.getUrl()), extractTitle(result.getContent()), result.getContent());
		}
		catch (final MalformedURLException e) {
			logger.error("MalformedURLException", e);
		}
	}

	protected String extractTitle(final String content) {
		final Document document = Jsoup.parse(content);
		final Elements a = document.getElementsByTag("title");
		for (final Element e : a) {
			return stringUtil.shorten(e.html(), TITLE_MAX_LENGTH);
		}
		return "-";
	}

}
