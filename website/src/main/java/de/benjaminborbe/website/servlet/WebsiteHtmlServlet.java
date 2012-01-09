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
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public abstract class WebsiteHtmlServlet extends HttpServlet {

	private static final long serialVersionUID = 1544940069187374367L;

	private static final String START_TIME = "start_time";

	protected final Logger logger;

	protected final CssResourceRenderer cssResourceRenderer;

	protected final JavascriptResourceRenderer javascriptResourceRenderer;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ParseUtil parseUtil;

	@Inject
	public WebsiteHtmlServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil) {
		this.logger = logger;
		this.cssResourceRenderer = cssResourceRenderer;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
	}

	protected abstract String getTitle();

	protected Collection<Widget> getWidgets() {
		return new HashSet<Widget>();
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
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final HttpContext context = new HttpContext();
		context.getData().put(START_TIME, getNowAsString());
		printHtml(request, response, context);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("printHtml");
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHead(request, response, context);
		printBody(request, response, context);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.debug("printBody");
		out.println("<body>");
		out.println("<h1>" + getTitle() + "</h1>");
		printFooter(request, response, context);
		out.println("</body>");
	}

	protected void printFooter(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.debug("printFooter");
		out.println("<div>");
		final long now = getNowAsLong();
		final long startTime = parseUtil.parseLong(context.getData().get(START_TIME), now);
		out.println("request takes " + (now - startTime) + " ms");
		out.println("</div>");
	}

	protected void printHead(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("printHead");
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("<title>" + getTitle() + "</title>");
		javascriptResourceRenderer.render(request, response, getJavascriptResources(request, response));
		cssResourceRenderer.render(request, response, getCssResources(request, response));
		out.println("</head>");
	}

	protected Collection<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.debug("getJavascriptResources");
		final Set<JavascriptResource> result = new HashSet<JavascriptResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.debug("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.debug("try widget " + widget.getClass().getName());
			if (widget instanceof RequireJavascriptResource) {
				logger.debug("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireJavascriptResource requireJavascriptResource = (RequireJavascriptResource) widget;
				result.addAll(requireJavascriptResource.getJavascriptResource(request, response));
			}
		}
		return result;
	}

	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		logger.debug("getCssResources");
		final Set<CssResource> result = new HashSet<CssResource>();
		final Collection<Widget> widgets = getWidgets();
		logger.debug("found " + widgets.size() + " widgets");
		for (final Widget widget : widgets) {
			logger.debug("try widget " + widget.getClass().getName());
			if (widget instanceof RequireCssResource) {
				logger.debug("widget " + widget.getClass().getName() + " requireJavascriptResource");
				final RequireCssResource requireCssResource = (RequireCssResource) widget;
				result.addAll(requireCssResource.getCssResource(request, response));
			}
		}
		return result;
	}

}
