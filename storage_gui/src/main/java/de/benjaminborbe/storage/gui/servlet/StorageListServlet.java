package de.benjaminborbe.storage.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.io.FlushPrintWriter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;

@Singleton
public class StorageListServlet extends WebsiteServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_PREFIX = "prefix";

	private final Logger logger;

	private final StorageService persistentStorageService;

	@Inject
	public StorageListServlet(
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
		final String prefix = request.getParameter(PARAMETER_PREFIX);
		if (columnFamily == null) {
			out.println("parameter " + PARAMETER_COLUMNFAMILY + " missing<br>");
		}
		if (columnFamily != null) {
			try {
				out.println("value=<br>");
				out.println("<pre>");
				final StorageIterator i;
				if (prefix != null) {
					i = persistentStorageService.findByIdPrefix(columnFamily, prefix);
				}
				else {
					i = persistentStorageService.list(columnFamily);
				}
				final List<String> keys = new ArrayList<String>();
				while (i.hasNext()) {
					keys.add(i.nextString());
				}
				Collections.sort(keys);
				out.println(StringUtils.join(keys, "\n"));
				out.println("</pre>");
			}
			catch (final Exception e) {
				out.printStackTrace(e);
			}
		}
		out.println("</body></html>");
	}
}
