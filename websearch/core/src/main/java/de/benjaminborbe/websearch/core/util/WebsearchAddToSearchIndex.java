package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.websearch.core.WebsearchConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import javax.inject.Inject;

public class WebsearchAddToSearchIndex {

	private static final int TITLE_MAX_LENGTH = 80;

	private final Logger logger;

	private final IndexService indexerService;

	private final HtmlUtil htmlUtil;

	private final StringUtil stringUtil;

	private final HttpUtil httpUtil;

	@Inject
	public WebsearchAddToSearchIndex(
		final Logger logger,
		final IndexService indexerService,
		final HtmlUtil htmlUtil,
		final StringUtil stringUtil,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.indexerService = indexerService;
		this.htmlUtil = htmlUtil;
		this.stringUtil = stringUtil;
		this.httpUtil = httpUtil;
	}

	public void addToIndex(final HttpResponse result) throws IndexerServiceException, ParseException {
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

	protected String extractTitle(final String content) throws ParseException {
		final Document document = Jsoup.parse(content);
		final Elements titleElements = document.getElementsByTag("title");
		for (final Element titleElement : titleElements) {
			return stringUtil.shorten(htmlUtil.filterHtmlTages(titleElement.html()), TITLE_MAX_LENGTH);
		}
		return "-";
	}

}
