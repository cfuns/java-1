package de.benjaminborbe.notification.gui.servlet;

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
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.notification.api.NotificationDto;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.gui.NotificationGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormInputTextareaWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class NotificationGuiSendServlet extends WebsiteHtmlServlet {

	private static final String TYPE = "manuel";

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Notification";

	private final Logger logger;

	private final NotificationService notificationService;

	private final AuthenticationService authenticationService;

	@Inject
	public NotificationGuiSendServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final CacheService cacheService,
			final NotificationService notificationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.notificationService = notificationService;
		this.authenticationService = authenticationService;
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

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final String from = request.getParameter(NotificationGuiConstants.PARAMETER_FROM);
			final String to = request.getParameter(NotificationGuiConstants.PARAMETER_TO);
			final String type = request.getParameter(NotificationGuiConstants.PARAMETER_TYPE);
			final String subject = request.getParameter(NotificationGuiConstants.PARAMETER_SUBJECT);
			final String message = request.getParameter(NotificationGuiConstants.PARAMETER_MESSAGE);
			if (from != null && to != null && message != null && subject != null && type != null) {
				final UserIdentifier fromUserIdentifier = authenticationService.createUserIdentifier(from);
				final UserIdentifier toUserIdentifier = authenticationService.createUserIdentifier(to);
				try {
					final NotificationDto notification = new NotificationDto();
					notification.setFrom(fromUserIdentifier);
					notification.setTo(toUserIdentifier);
					notification.setType(new NotificationTypeIdentifier(type));
					notification.setSubject(subject);
					notification.setMessage(message);
					notificationService.notify(sessionIdentifier, notification);
					widgets.add("send notification successful");
				}
				catch (final ValidationException e) {
					widgets.add("send notification failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget();
			form.addFormInputWidget(new FormInputTextWidget(NotificationGuiConstants.PARAMETER_FROM).addLabel("From:").addPlaceholder("username..."));
			form.addFormInputWidget(new FormInputTextWidget(NotificationGuiConstants.PARAMETER_TO).addLabel("To:").addPlaceholder("username..."));
			form.addFormInputWidget(new FormInputTextWidget(NotificationGuiConstants.PARAMETER_TYPE).addLabel("TYPE:").addDefaultValue(TYPE));
			form.addFormInputWidget(new FormInputTextWidget(NotificationGuiConstants.PARAMETER_SUBJECT).addLabel("Subject:").addPlaceholder("subject..."));
			form.addFormInputWidget(new FormInputTextareaWidget(NotificationGuiConstants.PARAMETER_MESSAGE).addLabel("Message:").addPlaceholder("message..."));
			form.addFormInputWidget(new FormInputSubmitWidget("send"));
			widgets.add(form);

			return widgets;
		}
		catch (final AuthenticationServiceException | NotificationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
