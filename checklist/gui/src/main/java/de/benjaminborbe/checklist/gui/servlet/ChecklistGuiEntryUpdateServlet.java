package de.benjaminborbe.checklist.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.gui.ChecklistGuiConstants;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class ChecklistGuiEntryUpdateServlet extends ChecklistGuiEntryFormServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Checklist - Entry-Update";

	private final ChecklistService checklistService;

	private final AuthenticationService authenticationService;

	@Inject
	public ChecklistGuiEntryUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final ChecklistService checklistService,
		final ChecklistGuiLinkFactory checklistGuiLinkFactory,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil,
			checklistGuiLinkFactory, cacheService);
		this.checklistService = checklistService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected String actionName() {
		return "update";
	}

	@Override
	protected ChecklistEntryIdentifier action(final SessionIdentifier sessionIdentifier, final ChecklistEntry checklistEntry) throws ChecklistServiceException,
		PermissionDeniedException, ValidationException, LoginRequiredException {
		checklistService.update(sessionIdentifier, checklistEntry);
		return checklistEntry.getId();
	}

	@Override
	protected ChecklistEntry getChecklist(final HttpServletRequest request) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
		AuthenticationServiceException {
		final String id = request.getParameter(ChecklistGuiConstants.PARAMETER_ENTRY_ID);
		final ChecklistEntryIdentifier checklistEntryIdentifier = new ChecklistEntryIdentifier(id);
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		return checklistService.read(sessionIdentifier, checklistEntryIdentifier);
	}

}
