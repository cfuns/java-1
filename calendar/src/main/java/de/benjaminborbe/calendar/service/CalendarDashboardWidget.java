package de.benjaminborbe.calendar.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;

@Singleton
public class CalendarDashboardWidget implements DashboardContentWidget {

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public CalendarDashboardWidget(final Logger logger, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil) {
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		out.println("today: " + calendarUtil.toDateString(now));
		out.println("<br>");
		out.println("now: " + calendarUtil.toDateTimeString(now));
		out.println("<br>");
		out.println("time: " + calendarUtil.getTime());
	}

	@Override
	public String getTitle() {
		return "CalendarWidget";
	}

	@Override
	public long getPriority() {
		return 1;
	}

}
