package de.benjaminborbe.projectile.gui.servlet;

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
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.projectile.gui.util.ProjectileLinkFactory;
import de.benjaminborbe.projectile.gui.util.UserComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class ProjectileGuiTeamViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -1955828859355943385L;

	private static final String TITLE = "Projectile - Team";

	private final Logger logger;

	private final ProjectileLinkFactory projectileLinkFactory;

	private final AuthenticationService authenticationService;

	private final ProjectileService projectileService;

	private final AuthorizationService authorizationService;

	@Inject
	public ProjectileGuiTeamViewServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final ProjectileLinkFactory projectileLinkFactory,
		final ProjectileService projectileService,
		final UrlUtil urlUtil,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.projectileLinkFactory = projectileLinkFactory;
		this.authenticationService = authenticationService;
		this.projectileService = projectileService;
		this.authorizationService = authorizationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ProjectileTeamIdentifier projectileTeamIdentifier = new ProjectileTeamIdentifier(request.getParameter(ProjectileGuiConstants.PARAMETER_TEAM_ID));
			final ProjectileTeam team = projectileService.getTeam(sessionIdentifier, projectileTeamIdentifier);
			final List<UserIdentifier> users = new ArrayList<>(projectileService.getUsersForTeam(sessionIdentifier, projectileTeamIdentifier));
			Collections.sort(users, new UserComparator());
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle() + " " + team.getName()));

			final String userId = request.getParameter(ProjectileGuiConstants.PARAMETER_USER_ID);
			if (userId != null) {
				final UserIdentifier userIdentifier = new UserIdentifier(userId);
				projectileService.addUserToTeam(sessionIdentifier, userIdentifier, projectileTeamIdentifier);
				throw new RedirectException(projectileLinkFactory.viewTeamUrl(request, projectileTeamIdentifier));
			}
			widgets.add(new H2Widget("Assigned Users"));
			final UlWidget ul = new UlWidget();
			if (users.isEmpty()) {
				widgets.add("no user found");
			} else {
				for (final UserIdentifier user : users) {
					final ListWidget row = new ListWidget();
					row.add(user.getId());
					row.add(" ");
					row.add(projectileLinkFactory.removeUserFromTeam(request, projectileTeamIdentifier, user));
					ul.add(row);
				}
				widgets.add(ul);
			}

			widgets.add(new H2Widget("Add User"));
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ProjectileGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ProjectileGuiConstants.PARAMETER_TEAM_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(ProjectileGuiConstants.PARAMETER_USER_ID).addLabel("Login:").addPlaceholder("login..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("add user"));
			widgets.add(formWidget);

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(ProjectileService.PERMISSION_ADMIN);
			authorizationService.expectPermission(sessionIdentifier, roleIdentifier);
		} catch (final AuthenticationServiceException | AuthorizationServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
