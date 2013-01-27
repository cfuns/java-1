package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.NetUtil;
import de.benjaminborbe.tools.util.ParseUtil;

public class RequestDurationWidget implements Widget {

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final NetUtil netUtil;

	@Inject
	public RequestDurationWidget(final Logger logger, final ParseUtil parseUtil, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final NetUtil netUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.netUtil = netUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();

		final long now = getNowAsLong();
		logger.trace("endTime = " + now);
		final long startTime = parseUtil.parseLong(context.getData().get(WebsiteConstants.START_TIME), now);
		final long duration = (now - startTime);
		logger.trace("duration = " + duration);
		final String msg = "request takes " + duration + " ms @ " + netUtil.getHostname();
		logger.trace(msg);
		out.print(msg);
	}

	private long getNowAsLong() {
		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		final Calendar now = calendarUtil.now(timeZone);
		return now.getTimeInMillis();
	}

}
