package de.benjaminborbe.poker.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.StringWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PokerGuiEventResetServlet extends WebsiteHtmlServlet {

	public static final String PARAMETER_ACTION = "action";

	public static final String ACTION_RESET = "reset";

	private static final String TITLE = "Poker - Reset Event";

	private static final String PARAMETER_CONFIRM = "confirm";

	private static final String YES = "yes";

	private static final long serialVersionUID = -5844202544448821460L;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final PokerService pokerService;

	@Inject
	public PokerGuiEventResetServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final PokerService pokerService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.pokerService = pokerService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(
		final HttpServletRequest request, final HttpServletResponse response, final HttpContext context
	) throws IOException, PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));
			if (ACTION_RESET.equals(request.getParameter(PARAMETER_ACTION))) {
				logger.debug("reset poker event");
				if (YES.equals(request.getParameter(PARAMETER_CONFIRM))) {
					logger.debug("confirm = " + YES);
					pokerService.resetEvent(sessionIdentifier);
					widgets.add("reset poker event done");
					return widgets;
				} else {
					logger.debug("confirm != " + YES);
					widgets.add(new StringWidget("enter " + YES));
				}
			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(PARAMETER_ACTION).addValue(ACTION_RESET));
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_CONFIRM).addLabel("Write '" + YES + "' to confirm reset"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("reset"));
			widgets.add(new StringWidget("Attention! This function reset the entire poker event!"));
			widgets.add(formWidget);
			return widgets;
		} catch (AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (PokerServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			pokerService.expectPokerAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		} catch (PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
