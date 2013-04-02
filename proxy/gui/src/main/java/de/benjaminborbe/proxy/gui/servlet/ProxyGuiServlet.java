package de.benjaminborbe.proxy.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Singleton
public class ProxyGuiServlet extends WebsiteServlet {

	public static final int TIMEOUT = 5000;

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final StreamUtil streamUtil;

	@Inject
	public ProxyGuiServlet(final Logger logger, final UrlUtil urlUtil, final AuthenticationService authenticationService, final AuthorizationService authorizationService, final CalendarUtil calendarUtil, final TimeZoneUtil timeZoneUtil, final Provider<HttpContext> httpContextProvider, final StreamUtil streamUtil) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.streamUtil = streamUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		InputStream inputStream = null;
		ServletOutputStream outputStream = null;
		try {
			logger.debug(request.getRequestURI());

			URL url = new URL("http://www.heise.de");
			final URLConnection connection = url.openConnection();
			connection.setReadTimeout(TIMEOUT);
			connection.setConnectTimeout(TIMEOUT);
			connection.connect();

			response.setContentType(connection.getContentType());

			inputStream = connection.getInputStream();
			outputStream = response.getOutputStream();
			streamUtil.copy(inputStream, outputStream);
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}
}
