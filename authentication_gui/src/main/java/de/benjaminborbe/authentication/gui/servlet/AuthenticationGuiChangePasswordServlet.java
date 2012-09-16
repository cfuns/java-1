package de.benjaminborbe.authentication.gui.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
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
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class AuthenticationGuiChangePasswordServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - Change Password";

	private static final String PARAMETER_PASSWORD_OLD = "password_old";

	private static final String PARAMETER_PASSWORD_NEW = "password_new";

	private static final String PARAMETER_PASSWORD_NEW_REPEAT = "password_new_repeat";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public AuthenticationGuiChangePasswordServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String password_old = request.getParameter(PARAMETER_PASSWORD_OLD);
			final String password_new = request.getParameter(PARAMETER_PASSWORD_NEW);
			final String password_repeat = request.getParameter(PARAMETER_PASSWORD_NEW_REPEAT);

			if (password_old != null && password_new != null && password_repeat != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				if (password_new.equals(password_repeat)) {
					widgets.add("password not match");
				}
				else {
					if (authenticationService.changePassword(sessionIdentifier, password_old, password_new)) {
						widgets.add("change password successful");
					}
					else {
						widgets.add("change password failed!");
					}
				}
			}
			final String action = request.getContextPath() + "/authentication/changePassword";
			final FormWidget form = new FormWidget(action).addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputTextWidget(PARAMETER_PASSWORD_OLD).addLabel("Old password").addPlaceholder("Old password ..."));
			form.addFormInputWidget(new FormInputTextWidget(PARAMETER_PASSWORD_NEW).addLabel("New password").addPlaceholder("New password ..."));
			form.addFormInputWidget(new FormInputTextWidget(PARAMETER_PASSWORD_NEW_REPEAT).addLabel("New password repeat").addPlaceholder("New password repeat ..."));
			form.addFormInputWidget(new FormInputSubmitWidget("change password"));
			widgets.add(form);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
