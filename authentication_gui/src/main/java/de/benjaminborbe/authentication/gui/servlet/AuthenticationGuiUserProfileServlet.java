package de.benjaminborbe.authentication.gui.servlet;

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
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.authentication.gui.util.AuthenticationGuiLinkFactory;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
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
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class AuthenticationGuiUserProfileServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3913165763528485400L;

	private static final String TITLE = "User - Profile";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthenticationGuiLinkFactory authenticationGuiLinkFactory;

	@Inject
	public AuthenticationGuiUserProfileServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final AuthenticationGuiLinkFactory authenticationGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authenticationGuiLinkFactory = authenticationGuiLinkFactory;
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
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String email = request.getParameter(AuthenticationGuiConstants.PARAMETER_EMAIL);
			final String fullname = request.getParameter(AuthenticationGuiConstants.PARAMETER_FULLNAME);
			final String timezone = request.getParameter(AuthenticationGuiConstants.PARAMETER_TIMEZONE);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final User user = authenticationService.getUser(sessionIdentifier, userIdentifier);

			if (email != null && fullname != null && timezone != null) {
				try {
					authenticationService.updateUser(sessionIdentifier, authenticationGuiLinkFactory.getEmailValidationUrl(request), email, fullname, timezone);

					widgets.add("update user completed");
				}
				catch (final ValidationException e) {
					widgets.add("update user failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_EMAIL).addLabel("Email").addPlaceholder("email...")
					.addDefaultValue(user.getEmail()));
			formWidget.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_FULLNAME).addLabel("Fullname").addPlaceholder("fullname...")
					.addDefaultValue(user.getFullname()));
			formWidget.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_TIMEZONE).addLabel("TimeZone").addPlaceholder("timeZone...")
					.addDefaultValue(authenticationService.getTimeZone(sessionIdentifier).getID()));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(formWidget);

			final ListWidget links = new ListWidget();
			links.add(authenticationGuiLinkFactory.changePassword(request));
			widgets.add(links);

			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
