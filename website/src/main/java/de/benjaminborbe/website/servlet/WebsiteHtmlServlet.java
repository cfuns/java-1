package de.benjaminborbe.website.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public abstract class WebsiteHtmlServlet extends HttpServlet {

	private static final long serialVersionUID = 1544940069187374367L;

	private static final String START_TIME = "start_time";

	protected final Logger logger;

	protected final CssResourceRenderer cssResourceRenderer;

	protected final JavascriptResourceRenderer javascriptResourceRenderer;

	protected final CalendarUtil calendarUtil;

	protected final TimeZoneUtil timeZoneUtil;

	protected final ParseUtil parseUtil;

	protected final NavigationWidget navigationWidget;

	protected final Provider<HttpContext> httpContextProvider;

	protected final AuthenticationService authenticationService;

	@Inject
	public WebsiteHtmlServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider) {
		this.logger = logger;
		this.cssResourceRenderer = cssResourceRenderer;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
		this.navigationWidget = navigationWidget;
		this.authenticationService = authenticationService;
		this.httpContextProvider = httpContextProvider;
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
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final HttpContext context = httpContextProvider.get();
		context.getData().put(START_TIME, getNowAsString());
		printHtml(request, response, context);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printHtml");
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHead(request, response, context);
		printBody(request, response, context);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.trace("printBody");
		out.println("<body>");
		printTop(request, response, context);
		printContent(request, response, context);
		printFooter(request, response, context);
		out.println("</body>");
	}

	protected void printTop(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("printTop");
			final ListWidget widgets = new ListWidget();
			widgets.add(navigationWidget);
			final SessionIdentifier sessionId = new SessionIdentifier(request);
			if (authenticationService.isLoggedIn(sessionId)) {
				widgets.add("logged in as " + authenticationService.getCurrentUser(sessionId));
				widgets.add(new LinkRelativWidget(request, "/authentication/logout", "logout"));
			}
			else {
				widgets.add(new LinkRelativWidget(request, "/authentication/login", "login"));
			}
			widgets.render(request, response, context);
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		widgets.render(request, response, context);
	}

	protected void printFooter(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.trace("printFooter");
		out.println("<div>");
		final long now = getNowAsLong();
		final long startTime = parseUtil.parseLong(context.getData().get(START_TIME), now);
		out.println("request takes " + (now - startTime) + " ms");
		out.println("</div>");
	}

	protected void printHead(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printHead");
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("<title>" + getTitle() + "</title>");
		javascriptResourceRenderer.render(request, response, getJavascriptResources(request, response));
		cssResourceRenderer.render(request, response, getCssResources(request, response));
		out.println("</head>");
	}

	protected Collection<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.trace("getJavascriptResources");
		final Set<JavascriptResource> result = new HashSet<JavascriptResource>();
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

}
