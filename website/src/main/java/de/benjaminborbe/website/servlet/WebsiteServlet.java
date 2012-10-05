package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.HtmlWidget;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public abstract class WebsiteServlet extends HttpServlet {

	private static final long serialVersionUID = -4012833048396011494L;

	private final AuthenticationService authenticationService;

	private final Provider<HttpContext> httpContextProvider;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final UrlUtil urlUtil;

	@Inject
	public WebsiteServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.httpContextProvider = httpContextProvider;
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service start");
		final String startTime = getNowAsString();
		logger.trace("service startTime=" + startTime);
		final HttpContext context = httpContextProvider.get();
		context.getData().put(WebsiteConstants.START_TIME, startTime);

		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (isLoginRequired() && !authenticationService.isLoggedIn(sessionIdentifier)) {
				final RedirectWidget widget = new RedirectWidget(buildLoginUrl(request));
				widget.render(request, response, context);
			}
			else {
				doService(request, response, context);
			}
		}
		catch (final AuthenticationServiceException e) {
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			widget.render(request, response, context);
		}
	}

	protected abstract void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException;

	protected boolean isLoginRequired() {
		return true;
	}

	private String getNowAsString() {
		return String.valueOf(getNowAsLong());
	}

	private long getNowAsLong() {
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		return now.getTimeInMillis();
	}

	protected String buildLoginUrl(final HttpServletRequest request) {
		final StringWriter result = new StringWriter();
		result.append(request.getContextPath());
		result.append("/authentication/login?referer=");
		try {
			result.append(buildReferer(request));
		}
		catch (final UnsupportedEncodingException e) {
			logger.info("buildReferer failed");
		}
		return result.toString();
	}

	private String buildReferer(final HttpServletRequest request) throws UnsupportedEncodingException {
		final StringWriter referer = new StringWriter();
		final String requestUri = request.getRequestURI().replaceFirst("//", "/");
		logger.trace("requestUri=" + requestUri);
		referer.append(requestUri);
		referer.append("?");
		@SuppressWarnings("unchecked")
		final Enumeration<String> e = request.getParameterNames();
		final List<String> pairs = new ArrayList<String>();
		while (e.hasMoreElements()) {
			final String name = e.nextElement();
			final String[] values = request.getParameterValues(name);
			for (final String value : values) {
				pairs.add(urlUtil.encode(name) + "=" + urlUtil.encode(value));
			}
		}
		referer.append(StringUtils.join(pairs, "&"));
		final String result = referer.toString();
		logger.trace("buildReferer => " + result);
		return urlUtil.encode(result);
	}

}
