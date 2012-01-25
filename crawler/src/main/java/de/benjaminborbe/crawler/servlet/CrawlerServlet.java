package de.benjaminborbe.crawler.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.crawler.api.CrawlerException;
import de.benjaminborbe.crawler.api.CrawlerService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.FormInputSubmitWidget;
import de.benjaminborbe.website.util.FormInputTextWidget;
import de.benjaminborbe.website.util.FormWidget;

@Singleton
public class CrawlerServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Crawler";

	private static final String PARAMETER_URL = "url";

	private final CrawlerService crawlerService;

	@Inject
	public CrawlerServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final CrawlerService crawlerService) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
		this.crawlerService = crawlerService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		logger.trace("printContent");
		out.println("<h1>" + getTitle() + "</h1>");
		if (request.getParameter(PARAMETER_URL) != null) {
			try {
				crawlerService.crawleDomain(request.getParameter(PARAMETER_URL));
				out.println("add url successful");
			}
			catch (final CrawlerException e) {
				out.println("<pre>");
				e.printStackTrace(out);
				out.println("</pre>");
			}
		}
		else {
			final String action = request.getContextPath() + "/crawler";
			final FormWidget formWidget = new FormWidget(action).addMethod("POST");
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_URL).addPlaceholder("url ..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("crawle"));
			formWidget.render(request, response, context);
		}
	}
}
