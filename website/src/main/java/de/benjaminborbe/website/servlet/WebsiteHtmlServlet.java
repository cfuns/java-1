package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.CssResourceWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.HtmlWidget;
import de.benjaminborbe.website.util.JavascriptResourceWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

@Singleton
public abstract class WebsiteHtmlServlet extends HttpServlet {

	private final class RequestDurationWidget implements Widget {

		private final long now;

		private RequestDurationWidget(final long now) {
			this.now = now;
		}

		@Override
		public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws PermissionDeniedException, IOException {
			final PrintWriter out = response.getWriter();
			final long startTime = parseUtil.parseLong(context.getData().get(START_TIME), now);
			final long duration = (now - startTime);
			final String msg = "request takes " + duration + " ms";
			logger.trace(msg);
			out.print(msg);
		}
	}

	private static final long serialVersionUID = 1544940069187374367L;

	private static final String START_TIME = "start_time";

	protected final Logger logger;

	protected final CalendarUtil calendarUtil;

	protected final TimeZoneUtil timeZoneUtil;

	protected final ParseUtil parseUtil;

	protected final NavigationWidget navigationWidget;

	protected final Provider<HttpContext> httpContextProvider;

	protected final AuthenticationService authenticationService;

	private final RedirectUtil redirectUtil;

	private final UrlUtil urlUtil;

	@Inject
	public WebsiteHtmlServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
		this.navigationWidget = navigationWidget;
		this.authenticationService = authenticationService;
		this.httpContextProvider = httpContextProvider;
		this.redirectUtil = redirectUtil;
		this.urlUtil = urlUtil;
	}

	protected abstract String getTitle();

	protected Collection<Widget> getWidgets() {
		final Set<Widget> widgets = new HashSet<Widget>();
		widgets.add(navigationWidget);
		return widgets;
	}

	protected long getNowAsLong() {
		final Calendar now = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		return now.getTimeInMillis();
	}

	protected String getNowAsString() {
		return String.valueOf(getNowAsLong());
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.trace("service");

		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (isLoginRequired() && !authenticationService.isLoggedIn(sessionIdentifier)) {
				response.sendRedirect(buildLoginUrl(request));
				return;
			}
		}
		catch (final AuthenticationServiceException e) {
			final PrintWriter out = response.getWriter();
			out.println("<html><head></head><body>");
			out.println("<pre>");
			e.printStackTrace(out);
			out.println("</pre>");
			out.println("</body></html>");
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final HttpContext context = httpContextProvider.get();
		context.getData().put(START_TIME, getNowAsString());
		try {
			final Widget widget = createHtmlWidget(request, response, context);
			widget.render(request, response, context);
		}
		catch (final PermissionDeniedException e) {
			final PrintWriter out = response.getWriter();
			out.println("<html><head></head><body>");
			out.println("<pre>");
			e.printStackTrace(out);
			out.println("</pre>");
			out.println("</body></html>");
		}
		catch (final RedirectException e) {
			redirectUtil.sendRedirect(request, response, e.getTarget());
			return;
		}
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

	protected String buildReferer(final HttpServletRequest request) throws UnsupportedEncodingException {
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

	protected Widget createHtmlWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, RedirectException,
			PermissionDeniedException {
		final ListWidget widgets = new ListWidget();
		logger.trace("printHtml");
		widgets.add(createHeadWidget(request, response, context));
		widgets.add(createBodyWidget(request, response, context));
		return new TagWidget("html", widgets);
	}

	protected Widget createBodyWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException,
			RedirectException {
		logger.trace("printBody");
		final ListWidget widgets = new ListWidget();
		widgets.add(createTopWidget(request, response, context));
		widgets.add(createContentWidget(request, response, context));
		widgets.add(createFooterWidget(request, response, context));
		return new TagWidget("body", widgets);
	}

	protected Widget createTopWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		try {
			logger.trace("printTop");
			final ListWidget widgets = new ListWidget();
			widgets.add(navigationWidget);
			final SessionIdentifier sessionId = authenticationService.createSessionIdentifier(request);
			if (authenticationService.isLoggedIn(sessionId)) {
				widgets.add("logged in as " + authenticationService.getCurrentUser(sessionId));
				widgets.add(new LinkRelativWidget(request, "/authentication/logout", "logout"));
			}
			else {
				widgets.add(new LinkRelativWidget(request, "/authentication/login", "login"));
			}
			return new TagWidget("div", widgets);
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		return widgets;
	}

	protected Widget createFooterWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printFooter");
		final ListWidget widgets = new ListWidget();
		final long now = getNowAsLong();
		widgets.add(new RequestDurationWidget(now));
		return new TagWidget("div", widgets);
	}

	protected Widget createHeadWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		logger.trace("printHead");
		final ListWidget widgets = new ListWidget();
		widgets.add(new TagWidget("title", getTitle()));
		widgets.add(new HtmlWidget("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"));
		widgets.add(new HtmlWidget("<meta http-equiv=\"content-language\" content=\"en\" />"));
		widgets.add(new HtmlWidget("<meta name=\"description\" content=\"BB\" />"));
		widgets.add(new HtmlWidget("<meta name=\"keywords\" content=\"BB\" />"));
		widgets.add(new HtmlWidget("<link rel=\"shortcut icon\" href=\"" + request.getContextPath() + "/images/favicon.gif\" />"));
		widgets.add(new JavascriptResourceWidget(getJavascriptResources(request, response)));
		widgets.add(new CssResourceWidget(getCssResources(request, response)));
		return new TagWidget("head", widgets);
	}

	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getJavascriptResources");
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.trace("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.trace("try widget " + widget.getClass().getName());
			if (widget instanceof RequireJavascriptResource) {
				logger.trace("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireJavascriptResource requireJavascriptResource = (RequireJavascriptResource) widget;
				result.addAll(requireJavascriptResource.getJavascriptResource(request, response));
			}
		}
		return result;
	}

	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getCssResources");
		final Set<CssResource> result = new HashSet<CssResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.trace("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.trace("try widget " + widget.getClass().getName());
			if (widget instanceof RequireCssResource) {
				logger.trace("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireCssResource requireCssResource = (RequireCssResource) widget;
				result.addAll(requireCssResource.getCssResource(request, response));
			}
		}

		// static
		final String contextPath = request.getContextPath();
		result.add(new CssResourceImpl(contextPath + "/css/style.css"));

		return result;
	}

	/**
	 * default login is required for each html-servlet
	 */
	protected boolean isLoginRequired() {
		return true;
	}
}
