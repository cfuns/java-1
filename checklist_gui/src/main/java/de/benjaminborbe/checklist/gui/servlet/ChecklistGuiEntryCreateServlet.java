package de.benjaminborbe.checklist.gui.servlet;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

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
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistEntryDto;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class ChecklistGuiEntryCreateServlet extends ChecklistGuiEntryFormServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Checklist - Entry-Create";

	private final ChecklistService checklistService;

	@Inject
	public ChecklistGuiEntryCreateServlet(
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
			final ChecklistGuiLinkFactory checklistGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, checklistService,
				checklistGuiLinkFactory);
		this.checklistService = checklistService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected String actionName() {
		return "create";
	}

	@Override
	protected ChecklistEntryIdentifier action(final SessionIdentifier sessionIdentifier, final ChecklistEntry checklistEntry) throws ChecklistServiceException,
			PermissionDeniedException, ValidationException, LoginRequiredException {
		return checklistService.create(sessionIdentifier, checklistEntry);
	}

	@Override
	protected ChecklistEntry getChecklist(final HttpServletRequest request) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException,
			AuthenticationServiceException {
		return new ChecklistEntryDto();
	}

}
