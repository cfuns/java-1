package de.benjaminborbe.lunch.util;

import com.google.inject.Inject;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LunchParseUtil {

	private final HtmlUtil htmlUtil;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public LunchParseUtil(final Logger logger, final HtmlUtil htmlUtil, final ParseUtil parseUtil) {
		this.logger = logger;
		this.htmlUtil = htmlUtil;
		this.parseUtil = parseUtil;
	}

	public Collection<String> extractSubscribedUser(final String htmlContent) {
		// logger.debug("htmlContent:\n" + htmlContent);
		final List<String> result = new ArrayList<>();
		final Document document = Jsoup.parse(htmlContent);
		final Elements tables = document.getElementsByTag("table");
		for (final Element table : tables) {
			if (isSubscriptTable(table)) {
				for (final Element tr : table.getElementsByTag("tr")) {
					final Elements tds = tr.getElementsByTag("td");
					if (!tds.isEmpty()) {
						final String name = tds.get(0).text();
						if (name != null) {
							final String nameTrimed = name.trim();
							if (nameTrimed.length() > 1) {
								logger.debug("found subscription for user: '" + nameTrimed + "'");
								result.add(nameTrimed);
							}
						}
					}
				}
			}
		}
		logger.debug("found " + result.size() + " subscribed users in htmlcontent");
		return result;
	}

	private boolean isSubscriptTable(final Element table) {
		final Elements trs = table.getElementsByTag("tr");
		if (trs != null && !trs.isEmpty()) {
			final Element head = trs.get(0);
			final Elements tds = head.getElementsByTag("th");
			if (tds != null && !tds.isEmpty()) {
				final String text = tds.get(0).text();
				return text != null && text.contains("Teilnehmer");
			}
		}
		return false;
	}

	public boolean extractLunchSubscribed(final String content, final String fullname) {
		return content.contains(fullname);
	}

	public String extractLunchName(final String htmlContent) throws ParseException {
		final Document document = Jsoup.parse(htmlContent);
		{
			final Elements elements = document.getElementsByClass("tipMacro");
			for (final Element element : elements) {
				for (final Element td : element.getElementsByTag("p")) {
					final String innerHtml = td.html();
					final String result = htmlUtil.filterHtmlTages(innerHtml);
					if (result != null && result.length() > 0) {
						logger.debug("found lunch lame " + result);
						return result;
					}
				}
			}
		}
		{
			int pos = 0;
			pos = parseUtil.indexOf(htmlContent, "ac:name=\"tip\"", pos);
			try {
				pos = parseUtil.indexOf(htmlContent, "INLINE", pos);
			} catch (final ParseException e) {
				// nop
			}
			final int pstart = parseUtil.indexOf(htmlContent, "<ac:rich-text-body>", pos);
			final int pend = parseUtil.indexOf(htmlContent, "</ac:rich-text-body>", pstart);
			final String result = htmlUtil.filterHtmlTages(htmlContent.substring(pstart, pend));
			if (result != null && result.length() > 0) {
				logger.debug("found lunch lame " + result);
				return result;
			}
		}

		logger.debug("extractLunchName failed " + htmlContent);
		return null;
	}
}
