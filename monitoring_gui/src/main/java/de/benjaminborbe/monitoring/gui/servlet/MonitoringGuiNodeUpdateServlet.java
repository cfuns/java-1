package de.benjaminborbe.monitoring.gui.servlet;

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
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
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
import de.benjaminborbe.website.form.FormSelectboxWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class MonitoringGuiNodeUpdateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 3764801991353392201L;

	private static final String TITLE = "Monitoring - Node - Update";

	private final MonitoringService monitoringService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public MonitoringGuiNodeUpdateServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final MonitoringService monitoringService,
			final UrlUtil urlUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.authenticationService = authenticationService;
		this.monitoringService = monitoringService;
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String id = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_ID);
			final String name = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_NAME);
			final String checkType = request.getParameter(MonitoringGuiConstants.PARAMETER_NODE_CHECK_TYPE);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final MonitoringNodeIdentifier monitoringNodeIdentifier = monitoringService.createNodeIdentifier(id);
			final MonitoringNode node = monitoringService.getNode(sessionIdentifier, monitoringNodeIdentifier);

			if (name != null && checkType != null) {
				try {
					updateNode(sessionIdentifier, monitoringNodeIdentifier, name, checkType);
				}
				catch (final ValidationException e) {
					widgets.add("update node failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget();
			form.addFormInputWidget(new FormInputHiddenWidget(MonitoringGuiConstants.PARAMETER_NODE_ID));
			form.addFormInputWidget(new FormInputTextWidget(MonitoringGuiConstants.PARAMETER_NODE_NAME).addLabel("Name:").addPlaceholder("name ...").addDefaultValue(node.getName()));
			final FormSelectboxWidget checkTypeInput = new FormSelectboxWidget(MonitoringGuiConstants.PARAMETER_NODE_CHECK_TYPE).addLabel("Type:");
			for (final MonitoringCheckType monitoringCheckType : MonitoringCheckType.values()) {
				checkTypeInput.addOption(monitoringCheckType.name(), monitoringCheckType.getTitle());
			}
			checkTypeInput.addDefaultValue(node.getCheckType());
			form.addFormInputWidget(checkTypeInput);
			form.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(form);

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

	private void updateNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier, final String name, final String checkTypeString)
			throws ValidationException, MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		MonitoringCheckType checkType = null;
		{
			try {
				checkType = parseUtil.parseEnum(MonitoringCheckType.class, checkTypeString);
			}
			catch (final ParseException e) {
				errors.add(new ValidationErrorSimple("illegal expire"));
			}
		}
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		}
		else {
			final MonitoringNodeDto nodeDto = new MonitoringNodeDto();
			nodeDto.setId(monitoringNodeIdentifier);
			nodeDto.setName(name);
			nodeDto.setCheckType(checkType);
			monitoringService.updateNode(sessionIdentifier, nodeDto);
		}
	}
}
