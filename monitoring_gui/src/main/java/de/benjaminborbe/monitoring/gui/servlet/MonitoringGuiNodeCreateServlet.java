package de.benjaminborbe.monitoring.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.monitoring.gui.util.MonitoringCheckComparator;
import de.benjaminborbe.monitoring.gui.util.MonitoringGuiLinkFactory;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormSelectboxWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class MonitoringGuiNodeCreateServlet extends MonitoringWebsiteHtmlServlet {

	private static final long serialVersionUID = 3764801991353392201L;

	private static final String TITLE = "Monitoring - Node - Create";

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	private final StringUtil stringUtil;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public MonitoringGuiNodeCreateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService,
			final UrlUtil urlUtil,
			final MonitoringGuiLinkFactory monitoringGuiLinkFactory,
			final StringUtil stringUtil,
			final ComparatorUtil comparatorUtil,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.authenticationService = authenticationService;
		this.monitoringService = monitoringService;
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.monitoringGuiLinkFactory = monitoringGuiLinkFactory;
		this.stringUtil = stringUtil;
		this.comparatorUtil = comparatorUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createMonitoringContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String parentId = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_PARENT_ID);
			final String name = stringUtil.trim(request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_NAME));
			final String checkType = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_CHECK_TYPE);
			final String referer = request.getParameter(MonitoringGuiConstants.PARAMETER_REFERER);

			final String active = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_ACTIVATED);
			final String silent = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_SILENT);

			final MonitoringNodeIdentifier monitoringNodeParentIdentifier = monitoringService.createNodeIdentifier(parentId);
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final MonitoringCheckIdentifier type = getType(checkType);
			final Collection<String> requiredParameters = monitoringService.getRequireParameter(sessionIdentifier, type);
			final Map<String, String> parameter = new HashMap<String, String>();
			for (final String requiredParameter : requiredParameters) {
				final String value = stringUtil.trim(request.getParameter(MonitoringGuiConstants.PARAMETER_PREFIX + requiredParameter));
				parameter.put(requiredParameter, (value == null || value.isEmpty()) ? null : value);
			}
			if (name != null && checkType != null) {
				try {
					createNode(sessionIdentifier, monitoringNodeParentIdentifier, name, checkType, parameter, active, silent);

					if (referer != null) {
						throw new RedirectException(referer);
					}
					else {
						throw new RedirectException(monitoringGuiLinkFactory.nodeListUrl(request));
					}
				}
				catch (final ValidationException e) {
					widgets.add("create node failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputHiddenWidget(MonitoringGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputTextWidget(MonitoringGuiConstants.PARAMETER_NODE_NAME).addLabel("Name:").addPlaceholder("name ..."));
			formWidget.addFormInputWidget(new FormInputTextWidget(MonitoringGuiConstants.PARAMETER_NODE_PARENT_ID).addLabel("Parent-Node:").addPlaceholder("parentId ..."));
			formWidget.addFormInputWidget(new FormCheckboxWidget(MonitoringGuiConstants.PARAMETER_NODE_ACTIVATED).addLabel("Activated:"));
			formWidget.addFormInputWidget(new FormCheckboxWidget(MonitoringGuiConstants.PARAMETER_NODE_SILENT).addLabel("Silent:"));
			final FormSelectboxWidget checkTypeInput = new FormSelectboxWidget(MonitoringGuiConstants.PARAMETER_NODE_CHECK_TYPE).addLabel("Type:");
			for (final MonitoringCheck monitoringCheckType : comparatorUtil.sort(monitoringService.getMonitoringCheckTypes(), new MonitoringCheckComparator())) {
				checkTypeInput.addOption(monitoringCheckType.getId().getId(), monitoringCheckType.getTitle());
			}
			checkTypeInput.addDefaultValue(monitoringService.getMonitoringCheckTypeDefault().getId());
			formWidget.addFormInputWidget(checkTypeInput);
			for (final String requiredParameter : requiredParameters) {
				formWidget.addFormInputWidget(new FormInputTextWidget(MonitoringGuiConstants.PARAMETER_PREFIX + requiredParameter).addLabel(requiredParameter).addPlaceholder("..."));
			}
			formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
			widgets.add(formWidget);

			return widgets;
		}
		catch (final MonitoringServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget exceptionWidget = new ExceptionWidget(e);
			return exceptionWidget;
		}
	}

	private MonitoringCheckIdentifier getType(final String checkType) throws MonitoringServiceException {
		final MonitoringCheck result = monitoringService.getMonitoringCheckTypeById(new MonitoringCheckIdentifier(checkType));
		if (result != null) {
			return result.getId();
		}
		else {
			return monitoringService.getMonitoringCheckTypeDefault();
		}
	}

	private void createNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeParentIdentifier, final String name,
			final String checkTypeString, final Map<String, String> parameter, final String activeString, final String silentString) throws ValidationException,
			MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		final MonitoringCheckIdentifier checkType = new MonitoringCheckIdentifier(checkTypeString);
		final boolean active = parseUtil.parseBoolean(activeString, false);
		final boolean silent = parseUtil.parseBoolean(silentString, false);

		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			final MonitoringNodeDto nodeDto = new MonitoringNodeDto();
			nodeDto.setName(name);
			nodeDto.setCheckType(checkType);
			nodeDto.setParameter(parameter);
			nodeDto.setSilent(silent);
			nodeDto.setActive(active);
			nodeDto.setParentId(monitoringNodeParentIdentifier);
			monitoringService.createNode(sessionIdentifier, nodeDto);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			monitoringService.expectMonitoringAdminPermission(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final MonitoringServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}
}
