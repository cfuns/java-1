package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;

import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.api.CacheServiceException;
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

	private final CacheService cacheService;

	@Inject
	public RequestDurationWidget(
			final Logger logger,
			final ParseUtil parseUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final NetUtil netUtil,
			final CacheService cacheService) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.netUtil = netUtil;
		this.cacheService = cacheService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();

		final String hostname = getHostname();
		final long now = getNowAsLong();
		logger.trace("endTime = " + now);
		final long startTime = parseUtil.parseLong(context.getData().get(WebsiteConstants.START_TIME), now);
		final long duration = (now - startTime);
		logger.trace("duration = " + duration);
		final String msg = "request takes " + duration + " ms @ " + hostname;
		logger.trace(msg);
		out.print(msg);
	}

	private String getHostname() throws SocketException {
		try {
			{
				final String hostname = cacheService.get("hostname");
				if (hostname != null) {
					return hostname;
				}
			}
			{
				final String hostname = netUtil.getHostname();
				cacheService.set("hostname", hostname);
				return hostname;
			}
		}
		catch (final CacheServiceException e) {
			return "unkown";
		}
	}

	private long getNowAsLong() {
		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		final Calendar now = calendarUtil.now(timeZone);
		return now.getTimeInMillis();
	}

}
