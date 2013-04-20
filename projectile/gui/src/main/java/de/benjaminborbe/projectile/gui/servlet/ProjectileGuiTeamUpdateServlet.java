package de.benjaminborbe.projectile.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
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
import de.benjaminborbe.projectile.api.ProjectileTeamDto;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.gui.ProjectileGuiConstants;
import de.benjaminborbe.projectile.gui.util.ProjectileLinkFactory;
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
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ProjectileGuiTeamUpdateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -1955828859355943385L;

	private static final String TITLE = "Projectile - Team - Update";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ProjectileService projectileService;

	private final ProjectileLinkFactory projectileLinkFactory;

	private final AuthorizationService authorizationService;

	@Inject
	public ProjectileGuiTeamUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final ProjectileService projectileService,
		final ProjectileLinkFactory projectileLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.projectileService = projectileService;
		this.projectileLinkFactory = projectileLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {

			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String id = request.getParameter(ProjectileGuiConstants.PARAMETER_TEAM_ID);
			final String name = request.getParameter(ProjectileGuiConstants.PARAMETER_TEAM_NAME);
			final String referer = request.getParameter(ProjectileGuiConstants.PARAMETER_REFERER);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ProjectileTeamIdentifier projectileTeamIdentifier = new ProjectileTeamIdentifier(id);
			final ProjectileTeam team = projectileService.getTeam(sessionIdentifier, projectileTeamIdentifier);

			if (id != null && name != null) {
				try {

					final ProjectileTeamDto teamDto = new ProjectileTeamDto();
					teamDto.setId(projectileTeamIdentifier);
					teamDto.setName(name);

					projectileService.updateTeam(sessionIdentifier, teamDto);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(projectileLinkFactory.teamListUrl(request));
					}
				} catch (final ValidationException e) {
					widgets.add("update team => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}

			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ProjectileGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ProjectileGuiConstants.PARAMETER_TEAM_ID).addValue(team.getId()));
			formWidget
				.addFormInputWidget(new FormInputTextWidget(ProjectileGuiConstants.PARAMETER_TEAM_NAME).addLabel("Team:").addPlaceholder("name...").addDefaultValue(team.getName()));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(formWidget);

			return widgets;
		} catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
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
