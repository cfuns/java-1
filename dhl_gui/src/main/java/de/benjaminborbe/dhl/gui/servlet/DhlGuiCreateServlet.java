package de.benjaminborbe.dhl.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.dhl.api.DhlService;
import de.benjaminborbe.dhl.api.DhlServiceException;
import de.benjaminborbe.dhl.gui.util.DhlWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class DhlGuiCreateServlet extends DhlWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Dhl - Add Tracking";

	private static final String PARAMETER_ID = "id";

	private static final String PARAMETER_ZIP = "zip";

	private final DhlService dhlService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public DhlGuiCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final DhlService dhlService,
			final AuthorizationService authorizationService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.dhlService = dhlService;
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

			final String id = request.getParameter(PARAMETER_ID);
			final String zip = request.getParameter(PARAMETER_ZIP);
			if (id != null && zip != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
					addTracking(sessionIdentifier, id, zip);
					throw new RedirectException(request.getContextPath() + "/dhl/list");
				}
				catch (final ValidationException e) {
					widgets.add("add tracking failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_ID).addLabel("Id").addPlaceholder("Id..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(PARAMETER_ZIP).addLabel("Zip").addPlaceholder("Zip..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("add tracking"));
			widgets.add(formWidget);
			return widgets;
		}
		catch (final DhlServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void addTracking(final SessionIdentifier sessionIdentifier, final String trackingNumber, final String zipString) throws ValidationException, DhlServiceException,
			LoginRequiredException, PermissionDeniedException {

		final List<ValidationError> errors = new ArrayList<ValidationError>();
		long zip = 0;
		try {
			zip = parseUtil.parseLong(zipString);
		}
		catch (final ParseException e) {
			errors.add(new ValidationErrorSimple("illegal zip"));
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			dhlService.addTracking(sessionIdentifier, trackingNumber, zip);
		}
	}
}
