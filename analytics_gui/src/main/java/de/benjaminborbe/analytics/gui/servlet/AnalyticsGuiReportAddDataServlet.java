package de.benjaminborbe.analytics.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportValueDto;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
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
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class AnalyticsGuiReportAddDataServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Analytics - Report add data";

	private final AnalyticsService analyticsService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	private final ParseUtil parseUtil;

	@Inject
	public AnalyticsGuiReportAddDataServlet(
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
			final AnalyticsService analyticsService,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.analyticsService = analyticsService;
		this.logger = logger;
		this.parseUtil = parseUtil;
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

			final String reportId = request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_ID);
			final String date = request.getParameter(AnalyticsGuiConstants.PARAMETER_DATE);
			final String value = request.getParameter(AnalyticsGuiConstants.PARAMETER_VALUE);
			if (reportId != null && date != null && value != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

				try {
					final AnalyticsReportIdentifier analyticsReportIdentifier = new AnalyticsReportIdentifier(reportId);
					addData(sessionIdentifier, analyticsReportIdentifier, date, value);
					widgets.add("add data => success");
				}
				catch (final ValidationException e) {
					widgets.add("add data => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(AnalyticsGuiConstants.PARAMETER_REPORT_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(AnalyticsGuiConstants.PARAMETER_DATE).addLabel("Date:").addPlaceholder("date..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(AnalyticsGuiConstants.PARAMETER_VALUE).addLabel("Value:").addPlaceholder("value..."));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("add"));
			widgets.add(formWidget);

			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AnalyticsServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void addData(final SessionIdentifier sessionIdentifier, final AnalyticsReportIdentifier analyticsReportIdentifier, final String dateString, final String valueString)
			throws AnalyticsServiceException, ValidationException, PermissionDeniedException, LoginRequiredException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		Calendar calendar = null;
		{
			try {
				calendar = calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), dateString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal date"));
			}
		}

		double value = 0;
		{
			try {
				value = parseUtil.parseDouble(valueString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal delay"));
			}
		}

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			final AnalyticsReportValueDto reportValue = new AnalyticsReportValueDto(calendar, value, 1l);
			analyticsService.addReportValue(analyticsReportIdentifier, reportValue);
		}
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			analyticsService.expectAnalyticsAdminPermission(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final AnalyticsServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
