package de.benjaminborbe.checklist.gui.servlet;

import com.google.common.collect.Lists;
import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.gui.ChecklistGuiConstants;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiEntryComparator;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiLinkFactory;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class ChecklistGuiEntryListServlet extends ChecklistGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Checklist - Entries";

	private final ChecklistService checklistService;

	private final Logger logger;

	private final ChecklistGuiLinkFactory linkFactory;

	private final AuthenticationService authenticationService;

	private final ChecklistGuiEntryComparator checklistEntryComparator;

	@Inject
	public ChecklistGuiEntryListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final ChecklistGuiLinkFactory linkFactory,
		final ChecklistService checklistService,
		final AuthorizationService authorizationService,
		final ChecklistGuiEntryComparator checklistEntryComparator,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.linkFactory = linkFactory;
		this.checklistService = checklistService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.checklistEntryComparator = checklistEntryComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createChecklistContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();

			final ChecklistListIdentifier id = new ChecklistListIdentifier(request.getParameter(ChecklistGuiConstants.PARAMETER_LIST_ID));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			{
				final ChecklistList checklistList = checklistService.read(sessionIdentifier, id);
				widgets.add(new H1Widget("Checklist - " + checklistList.getName()));
			}
			{
				final UlWidget ul = new UlWidget();
				final List<ChecklistEntry> entries = Lists.newArrayList(checklistService.getEntries(sessionIdentifier, id));
				Collections.sort(entries, checklistEntryComparator);
				for (final ChecklistEntry entry : entries) {
					final boolean completed = Boolean.TRUE.equals(entry.getCompleted());
					final ListWidget list = new ListWidget();
					if (completed) {
						list.add(linkFactory.entryUncomplete(request, entry.getId()));
					} else {
						list.add(linkFactory.entryComplete(request, entry.getId()));
					}
					list.add(" ");
					list.add(new SpanWidget(entry.getName()).addAttribute("class", "checklistEntryTitle"));
					list.add(" ");
					list.add(linkFactory.entryUpdate(request, entry.getId()));
					list.add(" ");
					list.add(linkFactory.entryDelete(request, entry.getId()));
					ul.add(new DivWidget(list).addClass(completed ? "completed" : "notCompleted"));
				}
				widgets.add(ul);
			}
			{
				final ListWidget navi = new ListWidget();
				navi.add(linkFactory.entryCreate(request, id));
				navi.add(" ");
				navi.add(linkFactory.listReset(request, id));
				navi.add(" ");
				widgets.add(navi);
			}
			return widgets;
		} catch (final ChecklistServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}
}
