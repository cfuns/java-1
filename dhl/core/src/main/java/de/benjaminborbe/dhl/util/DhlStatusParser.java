package de.benjaminborbe.dhl.util;

import javax.inject.Inject;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DhlStatusParser {

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ParseUtil parseUtil;

	private final HtmlUtil htmlUtil;

	@Inject
	public DhlStatusParser(final Logger logger, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final ParseUtil parseUtil, final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
		this.htmlUtil = htmlUtil;
	}

	public DhlStatus parseCurrentStatus(final Dhl dhl, final String htmlContent) {
		logger.trace("parseHtml for " + dhl.getTrackingNumber());
		final List<DhlStatus> all = parseAllStatus(dhl, htmlContent);
		final int size = all.size();
		logger.info("found " + size + " status");
		return size != 0 ? all.get(size - 1) : null;
	}

	public List<DhlStatus> parseAllStatus(final Dhl dhl, final String htmlContent) {
		logger.trace("parseHtml for " + dhl.getTrackingNumber());
		final List<DhlStatus> result = new ArrayList<>();
		final Document document = Jsoup.parse(htmlContent);
		final Elements tables = document.getElementsByAttributeValue("class", "full eventList");
		logger.trace("tables " + tables.size());
		for (final Element table : tables) {
			final Elements tbodies = table.getElementsByTag("tbody");
			logger.trace("tbodies " + tbodies.size());
			for (final Element tbody : tbodies) {
				final Elements trs = tbody.getElementsByTag("tr");
				logger.trace("trs " + trs.size());
				for (final Element tr : trs) {
					final Elements tds = tr.getElementsByAttributeValue("class", "overflow");
					logger.trace("tds " + tds.size());
					if (tds.size() == 3) {
						final Calendar calendar = parseDate(tds.get(0).html());
						final String place = parsePlace(tds.get(1).html());
						final String message = htmlUtil.unescapeHtml(parseMessage(tds.get(2).html()));
						result.add(new DhlStatus(dhl, calendar, place, message));
					}
				}
			}
		}
		return result;
	}

	protected String parseMessage(final String html) {
		return html != null ? html.trim() : null;
	}

	protected String parsePlace(final String html) {
		return html != null ? html.trim() : null;
	}

	protected Calendar parseDate(final String html) {
		final Pattern pattern = Pattern.compile("(\\d+).(\\d+).(\\d+) (\\d+):(\\d+)");
		final Matcher matcher = pattern.matcher(html);
		if (!matcher.find()) {
			return null;
		}
		try {
			final MatchResult matchResult = matcher.toMatchResult();
			final int date = parseUtil.parseInt(matchResult.group(1));
			final int month = parseUtil.parseInt(matchResult.group(2));
			final int year = parseUtil.parseInt(addYear(matchResult.group(3)));
			final int hour = parseUtil.parseInt(matchResult.group(4));
			final int minute = parseUtil.parseInt(matchResult.group(5));
			return calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), year, month - 1, date, hour, minute, 0);
		} catch (final ParseException e) {
			logger.info("parse date failed " + html);
			return null;
		}
	}

	public String addYear(final String year) {
		if (year.length() <= 2) {
			return "20" + year;
		}
		return year;
	}
}
