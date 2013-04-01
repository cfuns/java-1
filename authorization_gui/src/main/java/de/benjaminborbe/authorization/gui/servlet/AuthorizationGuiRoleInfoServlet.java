package de.benjaminborbe.authorization.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
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
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthorizationGuiRoleInfoServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - Role info";

	private final AuthorizationService authorizationSerivce;

	private final Logger logger;

	private final AuthorizationGuiLinkFactory authorizationGuiLinkFactory;

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
		final UrlUtil urlUtil,
		final AuthorizationGuiLinkFactory authorizationGuiLinkFactory,
		final AuthorizationService authorizationService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authorizationSerivce = authorizationSerivce;
		this.authorizationGuiLinkFactory = authorizationGuiLinkFactory;
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
			final RoleIdentifier roleIdentifier = authorizationSerivce.createRoleIdentifier(request.getParameter(AuthorizationGuiConstants.PARAMETER_ROLE_ID));
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " " + roleIdentifier));

			// users
			{
				widgets.add(new H2Widget("Users:"));
				final UlWidget ul = new UlWidget();

				for (final UserIdentifier userIdentifier : authorizationSerivce.getUsersWithRole(roleIdentifier)) {
					final ListWidget row = new ListWidget();
					row.add(authorizationGuiLinkFactory.userInfo(request, userIdentifier));
					row.add(" ");
					row.add(authorizationGuiLinkFactory.roleRemoveUser(request, roleIdentifier, userIdentifier));
					ul.add(row);
				}
				widgets.add(ul);
			}

			// permissions
			{
				widgets.add(new H2Widget("Permissions:"));
				final UlWidget ul = new UlWidget();
				for (final PermissionIdentifier permissionIdentifier : authorizationSerivce.getPermissions(roleIdentifier)) {
					final ListWidget row = new ListWidget();
					row.add(authorizationGuiLinkFactory.permissionInfo(request, permissionIdentifier));
					row.add(" ");
					row.add(authorizationGuiLinkFactory.roleRemovePermission(request, roleIdentifier, permissionIdentifier));
					ul.add(row);
				}
				widgets.add(ul);
			}

			final ListWidget links = new ListWidget();
			links.add(authorizationGuiLinkFactory.roleAddUser(request, roleIdentifier));
			links.add(" ");
			links.add(authorizationGuiLinkFactory.roleAddPermission(request, roleIdentifier));
			widgets.add(links);
			return widgets;
		} catch (final AuthorizationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}
}
