package de.benjaminborbe.websearch.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.LiWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class WebsearchGuiListPagesServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Websearch - List Pages";

	private final WebsearchService websearchService;

	private final DateUtil dateUtil;

	@Inject
	public WebsearchGuiListPagesServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final WebsearchService websearchService,
			final DateUtil dateUtil) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider);
		this.websearchService = websearchService;
		this.dateUtil = dateUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void printContent(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		final UlWidget ul = new UlWidget();
		for (final Page page : websearchService.getPages()) {
			ul.add(new LiWidget(buildPageWidget(page)));
		}
		widgets.add(ul);
		widgets.render(request, response, context);
	}

	protected String buildPageWidget(final Page page) {
		final StringWriter sw = new StringWriter();
		sw.append(page.getId().toExternalForm());
		sw.append(" ");
		if (page.getLastVisit() != null) {
			sw.append(dateUtil.dateTimeString(page.getLastVisit()));
		}
		else {
			sw.append("-");
		}
		return sw.toString();
	}
}
