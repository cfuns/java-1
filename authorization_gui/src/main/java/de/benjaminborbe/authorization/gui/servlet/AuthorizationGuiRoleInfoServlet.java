package de.benjaminborbe.authorization.gui.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

public class AuthorizationGuiRoleInfoServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - Role info";

	private final AuthorizationService authorizationSerivce;

	private final UrlUtil urlUtil;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public AuthorizationGuiRoleInfoServlet(
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
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.authorizationSerivce = authorizationSerivce;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String rolename = request.getParameter(AuthorizationGuiParameter.PARAMETER_ROLE);
			final RoleIdentifier roleIdentifier = authorizationSerivce.createRoleIdentifier(rolename);

			// users
			{
				widgets.add(new H2Widget("Users:"));
				final UlWidget ul = new UlWidget();
				for (final UserIdentifier userIdentifier : authenticationService.userList(sessionIdentifier)) {
					if (authorizationSerivce.hasRole(userIdentifier, roleIdentifier)) {
						ul.add(new LinkRelativWidget(urlUtil, request, "/authorization/user/info", new MapParameter().add(AuthorizationGuiParameter.PARAMETER_USER, userIdentifier.getId()),
								userIdentifier.getId()));
					}
				}
				widgets.add(ul);
			}

			// permissions
			{
				final UlWidget ul = new UlWidget();
				for (final PermissionIdentifier permissionIdentifier : authorizationSerivce.permissionList(roleIdentifier)) {
					ul.add(permissionIdentifier.getId());
				}
				widgets.add(ul);
				widgets.add(new LinkRelativWidget(urlUtil, request, "/authorization/role/addPermission", new MapParameter().add(AuthorizationGuiParameter.PARAMETER_ROLE,
						roleIdentifier.getId()), "add permission"));
			}
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
		catch (final AuthorizationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}
}
