package de.benjaminborbe.slash.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.slash.gui.util.SlashGuiRedirectDeterminer;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SlashGuiServlet extends WebsiteServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final SlashGuiRedirectDeterminer slashGuiRedirectDeterminer;

	@Inject
	public SlashGuiServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService,
		final SlashGuiRedirectDeterminer slashGuiRedirectDeterminer
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.slashGuiRedirectDeterminer = slashGuiRedirectDeterminer;
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException {
		logger.trace("service");
		response.sendRedirect(slashGuiRedirectDeterminer.getTarget(request));
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
