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
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

public class AuthorizationGuiRoleAddPermissionServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authorization - Role add Permission";

	private static final String PARAMETER_PERMISSION = "permisson";

	private static final String PARAMETER_ROLE = "role";

	private final AuthorizationService authorizationService;

	@Inject
	public AuthorizationGuiRoleAddPermissionServlet(
			final Logger logger,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, redirectUtil, urlUtil);
		this.authorizationService = authorizationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));
		try {
			final String roleName = request.getParameter(PARAMETER_ROLE);
			final String permissionName = request.getParameter(PARAMETER_PERMISSION);
			if (roleName != null && roleName.length() > 0 && permissionName != null && permissionName.length() > 0) {
				final PermissionIdentifier permission = authorizationService.createPermissionIdentifier(permissionName);
				final RoleIdentifier role = authorizationService.createRoleIdentifier(roleName);
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				if (authorizationService.addPermissionRole(sessionIdentifier, permission, role)) {
					widgets.add("success");
				}
				else {
					widgets.add("failed");
				}
			}
			else {
				final FormWidget form = new FormWidget("");
				form.addMethod(FormMethod.POST);
				form.addFormInputWidget(new FormInputHiddenWidget(PARAMETER_ROLE));
				form.addFormInputWidget(new FormInputTextWidget(PARAMETER_PERMISSION));
				form.addFormInputWidget(new FormInputSubmitWidget("add permission"));
				widgets.add(form);
			}
		}
		catch (final AuthorizationServiceException e) {
		}
		catch (final AuthenticationServiceException e) {
		}
		return widgets;
	}
}
