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
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.RedirectWidget;
import de.benjaminborbe.website.widget.HtmlWidget;

@Singleton
public abstract class WebsiteServlet extends HttpServlet {

	private static final long serialVersionUID = -4012833048396011494L;

	private final Provider<HttpContext> httpContextProvider;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final UrlUtil urlUtil;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	@Inject
	public WebsiteServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.httpContextProvider = httpContextProvider;
	}

	@Override
	protected final void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.trace("service start");
		final String startTime = getNowAsString();
		logger.trace("service startTime=" + startTime);
		final HttpContext context = httpContextProvider.get();
		context.getData().put(WebsiteConstants.START_TIME, startTime);

		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (!isEnabled()) {
				onDisabled(request, response, context);
			}
			else if ((isLoginRequired() || isAdminRequired()) && !authenticationService.isLoggedIn(sessionIdentifier)) {
				onLoginRequired(request, response, context);
			}
			else if (isAdminRequired() && !authorizationService.hasAdminRole(sessionIdentifier)) {
				onPermissionDenied(request, response, context);
			}
			else {
				try {
					doCheckPermission(request, response, context);
					doService(request, response, context);
				}
				catch (final LoginRequiredException e) {
					onLoginRequired(request, response, context);
				}
				catch (final PermissionDeniedException e) {
					onPermissionDenied(request, response, context);
				}
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			widget.render(request, response, context);
		}
		catch (final AuthorizationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final Widget widget = new HtmlWidget(new ExceptionWidget(e));
			widget.render(request, response, context);
		}
	}

	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
	}

	protected void onDisabled(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String url = buildPermissionDeniedUrl(request);
		logger.info("onDisabled - redirect: " + url);
		final RedirectWidget widget = new RedirectWidget(url);
		widget.render(request, response, context);
	}

	protected void onLoginRequired(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String url = buildLoginUrl(request);
		logger.info("loginRequired for servlet " + getClass().getSimpleName() + " - redirect: " + url);
		final RedirectWidget widget = new RedirectWidget(url);
		widget.render(request, response, context);
	}

	protected void onPermissionDenied(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String url = buildPermissionDeniedUrl(request);
		logger.info("permissionDenied - redirect: " + url);
		final RedirectWidget widget = new RedirectWidget(url);
		widget.render(request, response, context);
	}

	protected String buildLoginUrl(final HttpServletRequest request) {
		return buildRedirectUrl(request, "/authentication/login");
	}

	protected String buildPermissionDeniedUrl(final HttpServletRequest request) {
		return buildRedirectUrl(request, "/authorization/permissionDenied");
	}

	protected abstract void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException;

	private String getNowAsString() {
		return String.valueOf(getNowAsLong());
	}

	private long getNowAsLong() {
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		return now.getTimeInMillis();
	}

	protected String buildRedirectUrl(final HttpServletRequest request, final String target) {
		final StringWriter result = new StringWriter();
		result.append(request.getContextPath());
		result.append(target + "?referer=");
		try {
			result.append(buildReferer(request));
		}
		catch (final UnsupportedEncodingException e) {
			logger.info("buildReferer failed");
		}
		return result.toString();
	}

	protected String buildRefererUrl(final HttpServletRequest request) {
		final String referer = request.getHeader("referer");
		if (logger.isTraceEnabled())
			logger.trace("referer: " + referer);
		return referer;
	}

	private String buildReferer(final HttpServletRequest request) throws UnsupportedEncodingException {
		final StringWriter referer = new StringWriter();
		final String requestUri = request.getRequestURI().replaceFirst("//", "/");
		logger.trace("requestUri=" + requestUri);
		referer.append(requestUri);
		@SuppressWarnings("unchecked")
		final Enumeration<String> e = request.getParameterNames();
		if (e.hasMoreElements()) {
			referer.append("?");
		}
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

	/**
	 * default login is required for each servlet
	 */
	public boolean isLoginRequired() {
		return true;
	}

	/**
	 * default admin-role is required for each servlet
	 */
	public boolean isAdminRequired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
}
