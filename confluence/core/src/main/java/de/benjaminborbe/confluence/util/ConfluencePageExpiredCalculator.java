package de.benjaminborbe.confluence.util;

import com.google.inject.Inject;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import java.util.Calendar;

public class ConfluencePageExpiredCalculator {

	// 1 day
	private static final long EXPIRE_DAY = 24l * 60l * 60l * 1000l;

	private final CalendarUtil calendarUtil;

	private final Logger logger;

	@Inject
	public ConfluencePageExpiredCalculator(final Logger logger, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
	}

	public boolean isExpired(final Calendar lastVisit, final int maxLastVisitInDays, final Calendar lastModified, final Calendar pageModified) {

		if (lastVisit == null || lastModified == null) {
			logger.debug("expired because never visted before");
			return true;
		}
		if (lastModified != null && pageModified != null && lastModified.before(pageModified)) {
			logger.debug("expired because modified(" + calendarToString(pageModified) + ") after last visit(" + calendarToString(lastModified) + ")");
			return true;
		}

		final Calendar now = calendarUtil.now();
		final boolean result = now.getTimeInMillis() - lastVisit.getTimeInMillis() > maxLastVisitInDays * EXPIRE_DAY;
		logger.debug((result ? "" : "not ") + "expired - " + calendarToString(now) + " - " + calendarToString(lastVisit) + " > " + maxLastVisitInDays + " * " + EXPIRE_DAY);
		logger.debug((result ? "" : "not ") + "expired - " + (now.getTimeInMillis() - lastVisit.getTimeInMillis()) + " > " + (maxLastVisitInDays * EXPIRE_DAY));
		return result;
	}

	private String calendarToString(final Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendarUtil.toDateTimeString(calendar) + " (" + calendar.getTimeInMillis() + ")";
	}
}
