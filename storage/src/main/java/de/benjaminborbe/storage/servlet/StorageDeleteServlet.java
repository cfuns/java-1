package de.benjaminborbe.storage.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.tools.io.FlushPrintWriter;

@Singleton
public class StorageDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1048276599809672509L;

	private static final String PARAMETER_COLUMNFAMILY = "cf";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_KEY = "key";

	private final Logger logger;

	private final PersistentStorageService persistentStorageService;

	@Inject
	public StorageDeleteServlet(final Logger logger, final PersistentStorageService persistentStorageService) {
		this.logger = logger;
		this.persistentStorageService = persistentStorageService;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		logger.debug("StorageReadServlet.service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		final FlushPrintWriter out = new FlushPrintWriter(response.getWriter());
		out.println("<html><head></head><body>");
		final String columnFamily = request.getParameter(PARAMETER_COLUMNFAMILY);
		final String id = request.getParameter(PARAMETER_ID);
		final String key = request.getParameter(PARAMETER_KEY);

		if (columnFamily == null) {
			out.println("parameter " + PARAMETER_COLUMNFAMILY + " missing<br>");
		}
		if (id == null) {
			out.println("parameter " + PARAMETER_ID + " missing<br>");
		}
		if (key == null) {
			out.println("parameter " + PARAMETER_KEY + " missing<br>");
		}
		if (columnFamily != null && id != null && key != null) {
			try {
				persistentStorageService.delete(columnFamily, id, key);
				out.println("deleted");
			}
			catch (final Exception e) {
				out.printStackTrace(e);
			}
		}
		out.println("</body></html>");
	}
}
