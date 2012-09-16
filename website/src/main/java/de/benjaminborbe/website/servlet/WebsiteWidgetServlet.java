package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;

@Singleton
public abstract class WebsiteWidgetServlet extends HttpServlet {

	private static final long serialVersionUID = 4493141623771043143L;

	private final Provider<HttpContext> httpContextProvider;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public WebsiteWidgetServlet(final Logger logger, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final Provider<HttpContext> httpContextProvider) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.httpContextProvider = httpContextProvider;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("service start");
		final String startTime = getNowAsString();
		logger.trace("service startTime=" + startTime);
		final HttpContext context = httpContextProvider.get();
		context.getData().put(WebsiteConstants.START_TIME, startTime);
		final Widget widget = createWidget(request, response, context);
		widget.render(request, response, context);
		logger.debug("serice end");
	}

	public abstract Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException;

	private String getNowAsString() {
		return String.valueOf(getNowAsLong());
	}

	private long getNowAsLong() {
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		return now.getTimeInMillis();
	}

}
