package de.benjaminborbe.authorization.gui.servlet;

import java.io.IOException;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.gui.AuthorizationGuiConstants;
import de.benjaminborbe.authorization.gui.util.AuthorizationGuiLinkFactory;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class AuthorizationGuiUserInfoServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - User info";

	private final AuthorizationService authorizationSerivce;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthorizationGuiLinkFactory authorizationGuiLinkFactory;

	private final CalendarUtil calendarUtil;

	@Inject
	public AuthorizationGuiUserInfoServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final AuthorizationService authorizationSerivce,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final AuthorizationGuiLinkFactory authorizationGuiLinkFactory,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authorizationSerivce = authorizationSerivce;
		this.authenticationService = authenticationService;
		this.authorizationGuiLinkFactory = authorizationGuiLinkFactory;
		this.calendarUtil = calendarUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final String username = request.getParameter(AuthorizationGuiConstants.PARAMETER_USER_ID);
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " for " + username));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);

			final User user = authenticationService.getUser(sessionIdentifier, userIdentifier);
			widgets.add("email: " + user.getEmail());
			widgets.add(new BrWidget());
			widgets.add("fullname: " + user.getFullname());
			widgets.add(new BrWidget());
			widgets.add("emailVerified: " + asString(user.getEmailVerified()));
			widgets.add(new BrWidget());
			widgets.add("loginCounter: " + user.getLoginCounter());
			widgets.add(new BrWidget());
			widgets.add("loginDate: " + calendarUtil.toDateTimeString(user.getLoginDate()));
			widgets.add(new BrWidget());
			widgets.add("superAdmin: " + asString(user.getSuperAdmin()));
			widgets.add(new BrWidget());
			widgets.add("timeZone: " + asString(user.getTimeZone()));
			widgets.add(new BrWidget());

			widgets.add(new H2Widget("Roles"));
			for (final RoleIdentifier roleIdentifier : authorizationSerivce.getRoles(sessionIdentifier, userIdentifier)) {
				final ListWidget row = new ListWidget();
				row.add(authorizationGuiLinkFactory.roleInfo(request, roleIdentifier));
				row.add(" ");
				row.add(authorizationGuiLinkFactory.roleRemoveUser(request, roleIdentifier, userIdentifier));
				ul.add(row);
			}
			widgets.add(ul);
			widgets.add(authorizationGuiLinkFactory.userAddRole(request, userIdentifier));
			return widgets;
		}
		catch (final AuthenticationServiceException | AuthorizationServiceException e) {
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

	private String asString(final Boolean b) {
		return String.valueOf(Boolean.TRUE.equals(b));
	}

	private String asString(final TimeZone timeZone) {
		if (timeZone == null) {
			return "-";
		}
		return timeZone.getID();
	}
}
