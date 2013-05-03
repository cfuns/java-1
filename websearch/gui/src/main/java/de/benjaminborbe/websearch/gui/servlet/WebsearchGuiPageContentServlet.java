package de.benjaminborbe.websearch.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.websearch.gui.WebsearchGuiConstants;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class WebsearchGuiPageContentServlet extends WebsiteServlet {

	private static final long serialVersionUID = -682265109392123207L;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final WebsearchService websearchService;

	private final StreamUtil streamUtil;

	@Inject
	public WebsearchGuiPageContentServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final WebsearchService websearchService,
		final StreamUtil streamUtil
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.websearchService = websearchService;
		this.streamUtil = streamUtil;
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String url = request.getParameter(WebsearchGuiConstants.PARAMETER_PAGE_ID);
			final WebsearchPageIdentifier websearchPageIdentifier = websearchService.createPageIdentifier(url);
			final WebsearchPage websearchPage = websearchService.getPage(sessionIdentifier, websearchPageIdentifier);
			response.setContentType(websearchPage.getHeader().getValue("Content-Type"));

			final InputStream inputStream = websearchPage.getContent().getContentStream();
			final ServletOutputStream outputStream = response.getOutputStream();
			streamUtil.copy(inputStream, outputStream);
			inputStream.close();
			outputStream.close();
		} catch (WebsearchServiceException | AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		} finally {

		}
	}
}
