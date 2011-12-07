package de.benjaminborbe.storage.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class StorageListServlet extends HttpServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_PREFIX = "prefix";

	private final Logger logger;

	private final PersistentStorageService persistentStorageService;

	@Inject
	public StorageListServlet(final Logger logger, final PersistentStorageService persistentStorageService) {
		this.logger = logger;
		this.persistentStorageService = persistentStorageService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("StorageReadServlet.service");
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
				final List<String> keys;
				if (prefix != null) {
					keys = persistentStorageService.findByIdPrefix(columnFamily, prefix);
				}
				else {
					keys = persistentStorageService.list(columnFamily);
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
