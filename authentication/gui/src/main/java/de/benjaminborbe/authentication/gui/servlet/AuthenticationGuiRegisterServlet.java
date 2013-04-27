package de.benjaminborbe.authentication.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
import de.benjaminborbe.authentication.gui.config.AuthenticationGuiConfig;
import de.benjaminborbe.authentication.gui.util.AuthenticationGuiLinkFactory;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputPasswordWidget;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthenticationGuiRegisterServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - Register";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthenticationGuiConfig authenticationGuiConfig;

	private final AuthenticationGuiLinkFactory authenticationGuiLinkFactory;

	@Inject
	public AuthenticationGuiRegisterServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final AuthenticationService authenticationService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final AuthenticationGuiConfig authenticationGuiConfig,
		final AuthenticationGuiLinkFactory authenticationGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authenticationGuiConfig = authenticationGuiConfig;
		this.authenticationGuiLinkFactory = authenticationGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final String username = request.getParameter(AuthenticationGuiConstants.PARAMETER_USER_ID);
			final String email = request.getParameter(AuthenticationGuiConstants.PARAMETER_EMAIL);
			final String password = request.getParameter(AuthenticationGuiConstants.PARAMETER_PASSWORD);
			final String passwordRepeat = request.getParameter(AuthenticationGuiConstants.PARAMETER_PASSWORD_REPEAT);
			final String fullname = request.getParameter(AuthenticationGuiConstants.PARAMETER_FULLNAME);
			if (username != null && password != null && email != null && password.equals(passwordRepeat)) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				try {
					authenticationService.register(sessionIdentifier, authenticationGuiLinkFactory.getShortenUrl(request), authenticationGuiLinkFactory.getEmailValidationUrl(request),
						username, email, password);
					final String referer = request.getParameter(AuthenticationGuiConstants.PARAMETER_REFERER) != null ? request.getParameter(AuthenticationGuiConstants.PARAMETER_REFERER)
						: request.getContextPath();
					logger.trace("send redirect to: " + referer);
					throw new RedirectException(referer);
				} catch (final ValidationException e) {
					widgets.add("register failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}

			}
			final FormWidget form = new FormWidget().addMethod(FormMethod.POST);
			form.addFormInputWidget(new FormInputHiddenWidget(AuthenticationGuiConstants.PARAMETER_REFERER));
			form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_USER_ID).addLabel("Username").addPlaceholder("Username..."));
			form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_EMAIL).addLabel("Email").addPlaceholder("Email..."));
			form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_FULLNAME).addLabel("Fullname").addPlaceholder("Fullname..."));
			form.addFormInputWidget(new FormInputPasswordWidget(AuthenticationGuiConstants.PARAMETER_PASSWORD).addLabel("Password").addPlaceholder("Password..."));
			form.addFormInputWidget(new FormInputPasswordWidget(AuthenticationGuiConstants.PARAMETER_PASSWORD_REPEAT).addLabel("Password-Repeat").addPlaceholder("Password repeat..."));
			form.addFormInputWidget(new FormInputSubmitWidget("register"));
			widgets.add(form);
			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return authenticationGuiConfig.registerEnabled();
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
