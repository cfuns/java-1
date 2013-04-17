package de.benjaminborbe.checklist.gui.servlet;

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
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListDto;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.gui.ChecklistGuiConstants;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiLinkFactory;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public abstract class ChecklistGuiListFormServlet extends ChecklistGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Checklist - List-Update";

	private final AuthenticationService authenticationService;

	private final ChecklistGuiLinkFactory checklistGuiLinkFactory;

	private final Logger logger;

	@Inject
	public ChecklistGuiListFormServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final ChecklistGuiLinkFactory checklistGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.checklistGuiLinkFactory = checklistGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	protected abstract String actionName();

	@Override
	protected Widget createChecklistContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			if (requestHasRequiredParameter(request)) {
				try {

					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
					final ChecklistList checklistList = buildList(request);
					final ChecklistListIdentifier checklistListIdentifier = action(sessionIdentifier, checklistList);

					final String referer = request.getParameter(ChecklistGuiConstants.PARAMETER_REFERER);
					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(checklistGuiLinkFactory.entryListUrl(request, checklistListIdentifier));
					}
				} catch (final ValidationException e) {
					widgets.add(actionName() + " collection => failed");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			widgets.add(createFormWidget(request));
			return widgets;
		} catch (final ChecklistServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	private boolean requestHasRequiredParameter(final HttpServletRequest request) {
		return request.getParameter(ChecklistGuiConstants.PARAMETER_LIST_NAME) != null;
	}

	protected abstract ChecklistListIdentifier action(SessionIdentifier sessionIdentifier, ChecklistList checklistList) throws ChecklistServiceException, PermissionDeniedException,
		ValidationException, LoginRequiredException;

	private ChecklistList buildList(final HttpServletRequest request) throws ValidationException {
		final ChecklistListDto result = new ChecklistListDto();
		final List<ValidationError> errors = new ArrayList<>();
		result.setId(new ChecklistListIdentifier(request.getParameter(ChecklistGuiConstants.PARAMETER_LIST_ID)));
		result.setName(request.getParameter(ChecklistGuiConstants.PARAMETER_LIST_NAME));
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			return result;
		}
	}

	private FormWidget createFormWidget(final HttpServletRequest request) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
		AuthenticationServiceException {
		final ChecklistList checklistList = getChecklist(request);
		final FormWidget formWidget = new FormWidget();
		formWidget.addFormInputWidget(new FormInputHiddenWidget(ChecklistGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
		formWidget.addFormInputWidget(new FormInputHiddenWidget(ChecklistGuiConstants.PARAMETER_LIST_ID).addValue(checklistList.getId()));
		formWidget.addFormInputWidget(new FormInputTextWidget(ChecklistGuiConstants.PARAMETER_LIST_NAME).addLabel("Name...").addDefaultValue(checklistList.getName()));
		formWidget.addFormInputWidget(new FormInputSubmitWidget(actionName()));
		return formWidget;
	}

	protected abstract ChecklistList getChecklist(final HttpServletRequest request) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
		AuthenticationServiceException;

}
