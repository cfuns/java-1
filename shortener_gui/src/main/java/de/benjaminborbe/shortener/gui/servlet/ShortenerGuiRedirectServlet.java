package de.benjaminborbe.shortener.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class ShortenerGuiRedirectServlet extends WebsiteServlet {

	private static final long serialVersionUID = 8905461321676703447L;

	private final ShortenerService shortenerService;

	@Inject
	public ShortenerGuiRedirectServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final ShortenerService shortenerService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.shortenerService = shortenerService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final String uri = request.getRequestURI();
			final int slashpos = uri.lastIndexOf('/');
			final String token = uri.substring(slashpos + 1);
			final RedirectWidget widget = new RedirectWidget(shortenerService.getUrl(token).toExternalForm());
			widget.render(request, response, context);
		}
		catch (final ShortenerServiceException e) {
		}
	}

}
