package de.benjaminborbe.authentication.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.UserIdentifier;
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
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Singleton
public class AuthenticationGuiUserPasswordLostEmailServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - Lost Password";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthenticationGuiLinkFactory authenticationGuiLinkFactory;

	@Inject
	public AuthenticationGuiUserPasswordLostEmailServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final AuthenticationService authenticationService,
		final UrlUtil urlUtil,
		final AuthenticationGuiLinkFactory authenticationGuiLinkFactory,
		final AuthorizationService authorizationService,
		final CacheService cacheService
	) {
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
	protected Widget createContentWidget(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws IOException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(request.getParameter(AuthenticationGuiConstants.PARAMETER_USER_ID));
			final String email = request.getParameter(AuthenticationGuiConstants.PARAMETER_EMAIL);
			if (userIdentifier != null && email != null) {
				try {
					sendPasswordLostEmail(request, userIdentifier, email);
					widgets.add("password lost email sent successful");
				} catch (final ValidationException e) {
					widgets.add("password lost failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			} else {
				final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
				form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_USER_ID).addLabel("Username:").addPlaceholder("Username..."));
				form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_EMAIL).addLabel("Email:").addPlaceholder("Email..."));
				form.addFormInputWidget(new FormInputSubmitWidget("reset password"));
				widgets.add(form);
			}
			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void sendPasswordLostEmail(
		final HttpServletRequest request,
		final UserIdentifier userIdentifier,
		final String email
	) throws AuthenticationServiceException,
		ValidationException, UnsupportedEncodingException {
		final String shortenUrl = authenticationGuiLinkFactory.getShortenUrl(request);
		final String resetUrl = authenticationGuiLinkFactory.getPasswordResetUrl(request);
		authenticationService.sendPasswordLostEmail(shortenUrl, resetUrl, userIdentifier, email);
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

}
