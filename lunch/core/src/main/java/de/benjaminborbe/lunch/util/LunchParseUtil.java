package de.benjaminborbe.lunch.util;

import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LunchParseUtil {

	private final HtmlUtil htmlUtil;

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final StringUtil stringUtil;

	@Inject
	public LunchParseUtil(final Logger logger, final HtmlUtil htmlUtil, final ParseUtil parseUtil, final StringUtil stringUtil) {
		this.logger = logger;
		this.htmlUtil = htmlUtil;
		this.parseUtil = parseUtil;
		this.stringUtil = stringUtil;
	}

	public Collection<String> extractSubscribedUser(final String htmlContent) {
		// logger.debug("htmlContent:\n" + htmlContent);
		final List<String> result = new ArrayList<String>();
		final Document document = Jsoup.parse(htmlContent);
		final Elements tables = document.getElementsByTag("table");
		for (final Element table : tables) {
			if (isSubscriptTable(table)) {
				for (final Element tr : table.getElementsByTag("tr")) {
					final Elements tds = tr.getElementsByTag("td");
					if (!tds.isEmpty()) {
						final String name = tds.get(0).text();
						if (name != null) {
							final String nameTrimed = name;
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
		final boolean result = htmlUtil.unescapeHtml(content).contains(fullname);
		logger.debug("extractLunchSubscribed fullname: " + fullname + " => " + result);
		return result;
	}

	public String extractLunchNameFromTitle(final String title) throws ParseException {
		final Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s(Bastians|Mittagessen|\\s|-)*(.*?)");
		final Matcher matcher = pattern.matcher(title);
		if (!matcher.matches()) {
			return null;
		}
		final String result = stringUtil.trim(matcher.group(2));
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public String extractLunchNameFromContent(final String htmlContent) throws ParseException {
		final Document document = Jsoup.parse(htmlContent);
		{
			final Elements elements = document.getElementsByClass("tipMacro");
			for (final Element element : elements) {
				for (final Element td : element.getElementsByTag("p")) {
					final String innerHtml = td.html();
					final String result = stringUtil.trim(htmlUtil.filterHtmlTages(innerHtml));
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
			final String result = stringUtil.trim(htmlUtil.filterHtmlTages(htmlContent.substring(pstart, pend)));
			if (result != null && result.length() > 0) {
				logger.debug("found lunch name " + result);
				return result;
			}
		}

		logger.debug("extractLunchNameFromContent failed " + htmlContent);
		return null;
	}

	public String extractLunchNameFromTitleOrContent(final String title, final String htmlContent) throws ParseException {
		{
			final String name = stringUtil.trim(extractLunchNameFromContent(htmlContent));
			if (name != null && name.length() > 1) {
				logger.debug("lunch name found in content: '" + name + "'");
				return name;
			}
		}
		{
			final String name = stringUtil.trim(extractLunchNameFromTitle(title));
			if (name != null && name.length() > 1) {
				logger.debug("lunch name found in title: '" + name + "'");
				return name;
			}
		}
		logger.debug("no lunch name found");
		return null;
	}

}
