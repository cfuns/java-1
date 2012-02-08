package de.benjaminborbe.websearch.service;

import java.util.Date;

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
import de.benjaminborbe.websearch.page.PageBean;
import de.benjaminborbe.websearch.page.PageDao;

@Singleton
public class WebsearchCrawlerNotify implements CrawlerNotifier {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexerService indexerService;

	private final StringUtil stringUtil;

	private final PageDao pageDao;

	@Inject
	public WebsearchCrawlerNotify(final Logger logger, final IndexerService indexerService, final StringUtil stringUtil, final PageDao pageDao) {
		this.logger = logger;
		this.indexerService = indexerService;
		this.stringUtil = stringUtil;
		this.pageDao = pageDao;
	}

	@Override
	public void notifiy(final CrawlerResult result) {
		logger.debug("notify");
		addToIndex(result);
		updateLastVisit(result);
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
		final Elements a = document.getElementsByTag("title");
		for (final Element e : a) {
			return stringUtil.shorten(e.html(), TITLE_MAX_LENGTH);
		}
		return "-";
	}

}
