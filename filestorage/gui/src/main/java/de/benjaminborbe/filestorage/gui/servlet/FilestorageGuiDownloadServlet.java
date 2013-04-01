package de.benjaminborbe.filestorage.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.gui.FilestorageGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class FilestorageGuiDownloadServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Filestorage";

	private final Logger logger;

	private final UrlUtil urlUtil;

	private final FilestorageService filestorageService;

	@Inject
	public FilestorageGuiDownloadServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService, final FilestorageService filestorageService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.filestorageService = filestorageService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		try {
			final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntryIdentifier(urlUtil.parseId(request, FilestorageGuiConstants.PARAMETER_FILE_ID));
			final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
			if (filestorageEntry != null) {
				response.setContentType(filestorageEntry.getContentType());
				final byte[] content = filestorageEntry.getContent();
				response.setContentLength(content.length);
				final ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(content);
			} else {
				logger.warn("filestorageEntry not found: " + filestorageEntryIdentifier);
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (final Exception e) {
			logger.warn(e.getClass().getName(), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
