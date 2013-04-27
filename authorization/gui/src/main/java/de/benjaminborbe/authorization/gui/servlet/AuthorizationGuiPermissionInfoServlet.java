package de.benjaminborbe.authorization.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.gui.AuthorizationGuiConstants;
import de.benjaminborbe.authorization.gui.util.AuthorizationGuiLinkFactory;
import de.benjaminborbe.authorization.gui.util.RoleIdentifierComparator;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthorizationGuiPermissionInfoServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - Permission Info";

	private final AuthorizationService authorizationService;

	private final Logger logger;

	private final AuthorizationGuiLinkFactory authorizationGuiLinkFactory;

	private final ComparatorUtil comparatorUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public AuthorizationGuiPermissionInfoServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final AuthorizationGuiLinkFactory authorizationGuiLinkFactory,
		final ComparatorUtil comparatorUtil
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.authorizationService = authorizationService;
		this.logger = logger;
		this.authorizationGuiLinkFactory = authorizationGuiLinkFactory;
		this.comparatorUtil = comparatorUtil;
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
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(request.getParameter(AuthorizationGuiConstants.PARAMETER_PERMISSION_ID));

			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " " + permissionIdentifier));
			{
				widgets.add(new H2Widget("Roles"));
				final UlWidget ul = new UlWidget();
				for (final RoleIdentifier roleIdentifier : comparatorUtil.sort(authorizationService.getRoles(sessionIdentifier, permissionIdentifier), new RoleIdentifierComparator())) {
					ul.add(authorizationGuiLinkFactory.roleInfo(request, roleIdentifier));
				}
				widgets.add(ul);
			}
			final UlWidget ul = new UlWidget();
			ul.add(authorizationGuiLinkFactory.permissionAddRole(request, permissionIdentifier));
			widgets.add(ul);

			return widgets;
		} catch (final AuthorizationServiceException | AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}
}
