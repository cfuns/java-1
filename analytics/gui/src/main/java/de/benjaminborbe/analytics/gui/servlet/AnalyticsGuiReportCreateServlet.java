package de.benjaminborbe.analytics.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiLinkFactory;
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
import de.benjaminborbe.website.form.FormSelectboxWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AnalyticsGuiReportCreateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Analytics - Report create";

	private final AnalyticsService analyticsService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AnalyticsGuiLinkFactory analyticsGuiLinkFactory;

	private final ParseUtil parseUtil;

	@Inject
	public AnalyticsGuiReportCreateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final AnalyticsService analyticsService,
		final AnalyticsGuiLinkFactory analyticsGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.parseUtil = parseUtil;
		this.analyticsService = analyticsService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.analyticsGuiLinkFactory = analyticsGuiLinkFactory;
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

			final String name = request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_NAME);
			final String aggregation = request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_AGGREGATION);
			final String referer = request.getParameter(AnalyticsGuiConstants.PARAMETER_REFERER);
			if (name != null && aggregation != null) {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

				try {
					addData(sessionIdentifier, name, aggregation);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(analyticsGuiLinkFactory.reportListUrl(request));
					}
				} catch (final ValidationException e) {
					widgets.add("add report => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(AnalyticsGuiConstants.PARAMETER_REPORT_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(AnalyticsGuiConstants.PARAMETER_REPORT_NAME).addLabel("Name:").addPlaceholder("name..."));
			final FormSelectboxWidget aggregationSelectBox = new FormSelectboxWidget(AnalyticsGuiConstants.PARAMETER_REPORT_AGGREGATION).addLabel("Aggregation");
			for (final AnalyticsReportAggregation analyticsReportAggregation : AnalyticsReportAggregation.values()) {
				aggregationSelectBox.addOption(analyticsReportAggregation.name(), analyticsReportAggregation.name().toLowerCase());
			}
			formWidget.addFormInputWidget(aggregationSelectBox);
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create report"));
			widgets.add(formWidget);

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final AnalyticsServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void addData(
		final SessionIdentifier sessionIdentifier,
		final String name,
		final String aggregationString
	) throws AnalyticsServiceException, ValidationException,
		PermissionDeniedException, LoginRequiredException {
		final List<ValidationError> errors = new ArrayList<>();
		AnalyticsReportAggregation aggregation = null;
		try {
			aggregation = parseUtil.parseEnum(AnalyticsReportAggregation.class, aggregationString);
		} catch (final ParseException e) {
			errors.add(new ValidationErrorSimple("invalid aggregation"));
		}
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			final AnalyticsReportDto report = new AnalyticsReportDto();
			report.setName(name);
			report.setAggregation(aggregation);
			analyticsService.createReport(sessionIdentifier, report);
		}
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			analyticsService.expectAnalyticsAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | AnalyticsServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
