package de.benjaminborbe.microblog.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorageException;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class MicroblogServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Microblog";

	private final MicroblogRevisionStorage microblogRevisionStorage;

	@Inject
	public MicroblogServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final MicroblogRevisionStorage microblogRevisionStorage) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, httpContextProvider);
		this.microblogRevisionStorage = microblogRevisionStorage;
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
		out.println("latest revision: ");
		try {
			out.println(microblogRevisionStorage.getLastRevision());
		}
		catch (final MicroblogRevisionStorageException e) {
			logger.debug("MicroblogRevisionStorageException", e);
			out.println("-");
		}
	}

}
