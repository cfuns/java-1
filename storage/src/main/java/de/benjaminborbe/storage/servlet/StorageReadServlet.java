package de.benjaminborbe.storage.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.io.FlushPrintWriter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;

@Singleton
public class StorageReadServlet extends WebsiteServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageReadServlet(
			final Logger logger,
			final StorageService persistentStorageService,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final AuthenticationService authenticationService,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.persistentStorageService = persistentStorageService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		logger.trace("StorageReadServlet.service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("<html><head></head><body>");
		final String columnFamily = request.getParameter(PARAMETER_COLUMNFAMILY);
		final String id = request.getParameter(PARAMETER_ID);
		final String key = request.getParameter(PARAMETER_KEY);

		if (columnFamily != null && id != null && key != null) {
			try {
				out.println("value=<br>");
				out.println("<pre>");
				out.println(StringEscapeUtils.escapeHtml(persistentStorageService.get(columnFamily, id, key)));
				out.println("</pre>");
			}
			catch (final Exception e) {
				out.printStackTrace(e);
			}
		}
		else {
			out.println("missing parameters.<br>usage: ");
			out.println(PARAMETER_COLUMNFAMILY + "=[Column_family]&" + PARAMETER_ID + "=[ID]&" + PARAMETER_KEY + "=[colname]<br>");
			out.println("example to read api-data: " + PARAMETER_COLUMNFAMILY + "=api_data&" + PARAMETER_ID + "=[content_key from database]&" + PARAMETER_KEY + "=content<br>");
		}
		out.println("</body></html>");
	}
}
