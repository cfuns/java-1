package de.benjaminborbe.authorization.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

public class AuthorizationGuiUserAddRoleServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - User add Role";

	private static final String PARAMETER_USERNANE = "user";

	private static final String PARAMETER_ROLENANE = "role";

	private final AuthorizationService authorizationService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public AuthorizationGuiUserAddRoleServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final AuthorizationService authorizationService,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.authorizationService = authorizationService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String username = request.getParameter(PARAMETER_USERNANE);
			final String rolename = request.getParameter(PARAMETER_ROLENANE);
			final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(username);
			final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(rolename);
			if (username != null && rolename != null && authorizationService.addUserRole(sessionIdentifier, userIdentifier, roleIdentifier)) {
				throw new RedirectException(request.getContextPath() + "/authorization/role");
			}
			else {
				final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
				formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_USERNANE).addLabel("Username").addPlaceholder("Username ..."));
				formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_ROLENANE).addLabel("Rolename").addPlaceholder("Rolename ..."));
				formWidget.addFormInputWidget(new FormInputSubmitWidget("grant"));
				widgets.add(formWidget);
				return widgets;
			}
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthorizationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
