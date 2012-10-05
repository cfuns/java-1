package de.benjaminborbe.search.gui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;

@Singleton
public class SearchGuiServiceComponentsServlet extends WebsiteServlet {

	private static final long serialVersionUID = 7928536214812474981L;

	private static final String TITLE = "SearchServiceComponents";

	private final Logger logger;

	private final SearchService searchService;

	@Inject
	public SearchGuiServiceComponentsServlet(
			final Logger logger,
			final SearchService searchService,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.searchService = searchService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		logger.trace("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHeader(out);
		printBody(out);
		printFooter(out);
		out.println("</html>");
	}

	protected void printBody(final PrintWriter out) {
		out.println("<h1>" + TITLE + "</h1>");
		final Collection<String> cs = searchService.getSearchComponentNames();
		out.println("found " + cs.size() + " SearchServiceComponents<br>");
		out.println("<ul>");
		for (final String s : cs) {
			out.println("<li>");
			out.println(s);
			out.println("</li>");
		}
		out.println("</ul>");
	}

	protected void printHeader(final PrintWriter out) {
		out.println("<head>");
		out.println("<title>" + TITLE + "</title>");
		out.println("</head>");
	}

	protected void printFooter(final PrintWriter out) {
	}

}
