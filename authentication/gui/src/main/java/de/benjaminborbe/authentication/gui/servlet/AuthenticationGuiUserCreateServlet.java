package de.benjaminborbe.authentication.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authentication.api.UserDto;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.gui.AuthenticationGuiConstants;
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
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthenticationGuiUserCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Authentication - User Create";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthenticationGuiLinkFactory authenticationGuiLinkFactory;

	@Inject
	public AuthenticationGuiUserCreateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final AuthenticationService authenticationService,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final AuthenticationGuiLinkFactory authenticationGuiLinkFactory) {
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
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, LoginRequiredException, RedirectException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String username = request.getParameter(AuthenticationGuiConstants.PARAMETER_USER_ID);
			final String email = request.getParameter(AuthenticationGuiConstants.PARAMETER_EMAIL);
			final String referer = request.getParameter(AuthenticationGuiConstants.PARAMETER_REFERER);

			if (username != null && email != null) {
				try {
					final SessionIdentifier sessionId = authenticationService.createSessionIdentifier(request);
					createUser(sessionId, username, email);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(authenticationGuiLinkFactory.userListUrl(request));
					}
				} catch (final ValidationException e) {
					widgets.add("create user failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget();
			form.addFormInputWidget(new FormInputHiddenWidget(AuthenticationGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_USER_ID).addLabel("Username:"));
			form.addFormInputWidget(new FormInputTextWidget(AuthenticationGuiConstants.PARAMETER_EMAIL).addLabel("Email:"));
			form.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(form);

			return widgets;
		} catch (final AuthenticationServiceException | SuperAdminRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void createUser(final SessionIdentifier sessionId, final String username, final String email) throws ValidationException, AuthenticationServiceException,
		LoginRequiredException, SuperAdminRequiredException {
		final UserDto userDto = new UserDto();
		userDto.setEmail(email);
		userDto.setId(new UserIdentifier(username));
		authenticationService.createUser(sessionId, userDto);
	}
}
