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
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.authentication.gui.util.AuthenticationGuiLinkFactory;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputPasswordWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class AuthenticationGuiChangePasswordServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - Change Password";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthenticationGuiLinkFactory authenticationGuiLinkFactory;

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
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final AuthenticationGuiLinkFactory authenticationGuiLinkFactory,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authenticationGuiLinkFactory = authenticationGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String password_old = request.getParameter(AuthenticationGuiConstants.PARAMETER_PASSWORD_OLD);
			final String password_new = request.getParameter(AuthenticationGuiConstants.PARAMETER_PASSWORD_NEW);
			final String password_repeat = request.getParameter(AuthenticationGuiConstants.PARAMETER_PASSWORD_NEW_REPEAT);

			if (password_old != null && password_new != null && password_repeat != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				try {
					authenticationService.changePassword(sessionIdentifier, password_old, password_new, password_repeat);
					widgets.add("change password successful");
				}
				catch (final ValidationException e) {
					widgets.add("change password failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputPasswordWidget(AuthenticationGuiConstants.PARAMETER_PASSWORD_OLD).addLabel("Old password").addPlaceholder("Old password..."));
			form.addFormInputWidget(new FormInputPasswordWidget(AuthenticationGuiConstants.PARAMETER_PASSWORD_NEW).addLabel("New password").addPlaceholder("New password..."));
			form.addFormInputWidget(new FormInputPasswordWidget(AuthenticationGuiConstants.PARAMETER_PASSWORD_NEW_REPEAT).addLabel("New password repeat").addPlaceholder(
					"New password repeat..."));
			form.addFormInputWidget(new FormInputSubmitWidget("change password"));
			widgets.add(form);

			final ListWidget links = new ListWidget();
			links.add(authenticationGuiLinkFactory.userProfile(request));
			widgets.add(links);

			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
