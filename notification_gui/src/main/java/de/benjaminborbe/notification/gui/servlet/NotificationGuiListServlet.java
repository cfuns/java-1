package de.benjaminborbe.notification.gui.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.gui.util.NotificationGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class NotificationGuiListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Notification";

	private final Logger logger;

	private final NotificationService notificationService;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final NotificationGuiLinkFactory notificationGuiLinkFactory;

	@Inject
	public NotificationGuiListServlet(
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
			final NotificationGuiLinkFactory notificationGuiLinkFactory,
			final NotificationService notificationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authorizationService = authorizationService;
		this.notificationGuiLinkFactory = notificationGuiLinkFactory;
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

			final Collection<NotificationTypeIdentifier> notificationTypeIdentifiers = notificationService.getNotificationTypeIdentifiers();
			if (notificationTypeIdentifiers.isEmpty()) {
				widgets.add("no type found");
				widgets.add(new BrWidget());
			}
			else {
				final UlWidget ul = new UlWidget();
				final Collection<NotificationMediaIdentifier> ms = notificationService.getNotificationMediaIdentifiers();
				for (final NotificationTypeIdentifier notificationTypeIdentifier : notificationTypeIdentifiers) {
					final ListWidget row = new ListWidget();
					row.add(notificationTypeIdentifier.getId());
					for (final NotificationMediaIdentifier notificationMediaIdentifier : ms) {
						row.add(" ");
						if (notificationService.isActive(sessionIdentifier, notificationMediaIdentifier, notificationTypeIdentifier)) {
							row.add(notificationGuiLinkFactory.remove(request, notificationMediaIdentifier, notificationTypeIdentifier));
						}
						else {
							row.add(notificationGuiLinkFactory.add(request, notificationMediaIdentifier, notificationTypeIdentifier));
						}
					}
					ul.add(row);
				}
				widgets.add(ul);
			}

			if (authorizationService.hasAdminRole(sessionIdentifier)) {
				widgets.add(notificationGuiLinkFactory.send(request));
			}

			return widgets;
		}
		catch (final AuthenticationServiceException | NotificationServiceException | AuthorizationServiceException e) {
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
