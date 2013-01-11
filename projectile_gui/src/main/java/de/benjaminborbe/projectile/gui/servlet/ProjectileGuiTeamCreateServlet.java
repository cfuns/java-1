package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileTeamDto;
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

@Singleton
public class ProjectileGuiTeamCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -1955828859355943385L;

	private static final String TITLE = "Projectile - Team - Create";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ProjectileService projectileService;

	private final ProjectileLinkFactory projectileLinkFactory;

	@Inject
	public ProjectileGuiTeamCreateServlet(
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
			final ProjectileLinkFactory projectileLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.authenticationService = authenticationService;
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

			final String name = request.getParameter(ProjectileGuiConstants.PARAMETER_TEAM_NAME);
			final String referer = request.getParameter(ProjectileGuiConstants.PARAMETER_REFERER);
			if (name != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

					final ProjectileTeamDto team = new ProjectileTeamDto();
					team.setName(name);

					projectileService.createTeam(sessionIdentifier, team);

					if (referer != null) {
						throw new RedirectException(referer);
					}
					else {
						throw new RedirectException(projectileLinkFactory.teamListUrl(request));
					}
				}
				catch (final ValidationException e) {
					widgets.add("create team => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}

			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(ProjectileGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(ProjectileGuiConstants.PARAMETER_TEAM_NAME).addLabel("Team:").addPlaceholder("name..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);

			return widgets;
		}
		catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

}
