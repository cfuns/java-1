package de.benjaminborbe.lunch.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.html.HtmlUtil;

public class LunchParseUtil {

	private final HtmlUtil htmlUtil;

	private final Logger logger;

	@Inject
	public LunchParseUtil(final Logger logger, final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.htmlUtil = htmlUtil;
	}

	public boolean extractLunchSubscribed(final String htmlContent, final String fullname) {
		final Document document = Jsoup.parse(htmlContent);
		final Elements tables = document.getElementsByClass("confluenceTable");
		for (final Element table : tables) {
			final Elements tds = table.getElementsByTag("td");
			for (final Element td : tds) {
				if (td.html().indexOf(fullname) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	public String extractLunchName(final String htmlContent) {
		// final String content = htmlContent.replaceAll("ac:", "ac");
		final Document document = Jsoup.parse(htmlContent);
		// System.err.println(document.toString());
		{
			final Elements elements = document.getElementsByClass("tipMacro");
			for (final Element element : elements) {
				for (final Element td : element.getElementsByTag("p")) {
					final String innerHtml = td.html();
					final String result = htmlUtil.filterHtmlTages(innerHtml);
					if (result != null && result.length() > 0) {
						return result;
					}
				}
			}
		}
		{
			final int pos = htmlContent.indexOf("ac:name=\"tip\"");
			final int pos2 = htmlContent.indexOf("INLINE", pos);
			final int pstart = htmlContent.indexOf("<p>", pos2);
			final int pend = htmlContent.indexOf("</p>", pos2);
			final String result = htmlUtil.filterHtmlTages(htmlContent.substring(pstart, pend));
			if (result != null && result.length() > 0) {
				return result;
			}
		}
		// logger.warn(content);
		// System.out.println(content);

		return null;
	}
}