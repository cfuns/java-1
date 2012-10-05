package de.benjaminborbe.storage.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class StorageWriteServlet extends WebsiteServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private static final String PARAMETER_VALUE = "value";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageWriteServlet(
			final Logger logger,
			final StorageService persistentStorageService,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
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
		final String value = request.getParameter(PARAMETER_VALUE);

		if (columnFamily == null) {
			out.println("parameter " + PARAMETER_COLUMNFAMILY + " missing<br>");
		}
		if (id == null) {
			out.println("parameter " + PARAMETER_ID + " missing<br>");
		}
		if (key == null) {
			out.println("parameter " + PARAMETER_KEY + " missing<br>");
		}
		if (value == null) {
			out.println("parameter " + PARAMETER_VALUE + " missing<br>");
		}
		if (columnFamily != null && id != null && key != null && value != null) {
			try {
				persistentStorageService.set(columnFamily, id, key, value);
				out.println("write");
			}
			catch (final Exception e) {
				out.printStackTrace(e);
			}
		}
		out.println("</body></html>");
	}
}
