package de.benjaminborbe.gallery.gui.util;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class GalleryGuiWebsiteServlet extends WebsiteServlet {

	private final AuthenticationService authenticationService;

	private final GalleryService galleryService;

	public GalleryGuiWebsiteServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final GalleryService galleryService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.authenticationService = authenticationService;
		this.galleryService = galleryService;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException, PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			galleryService.expectPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | GalleryServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

}
