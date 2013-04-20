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
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiLinkFactory;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiListComparator;
import de.benjaminborbe.checklist.gui.util.ChecklistGuiWebsiteHtmlServlet;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class ChecklistGuiListListServlet extends ChecklistGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Checklist - List";

	private final ChecklistService checklistService;

	private final Logger logger;

	private final ChecklistGuiLinkFactory linkFactory;

	private final AuthenticationService authenticationService;

	private final ChecklistGuiListComparator checklistListComparator;

	@Inject
	public ChecklistGuiListListServlet(
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
		final ChecklistGuiListComparator checklistListComparator,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.linkFactory = linkFactory;
		this.checklistService = checklistService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.checklistListComparator = checklistListComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createChecklistContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));
			final UlWidget ul = new UlWidget();
			final List<ChecklistList> lists = Lists.newArrayList(checklistService.getLists(sessionIdentifier));
			Collections.sort(lists, checklistListComparator);
			for (final ChecklistList checklistList : lists) {
				final ListWidget list = new ListWidget();
				list.add(linkFactory.entryList(request, checklistList.getId(), checklistList.getName()));
				list.add(" ");
				list.add(linkFactory.listUpdate(request, checklistList.getId()));
				list.add(" ");
				list.add(linkFactory.listDelete(request, checklistList.getId()));
				ul.add(list);
			}
			widgets.add(ul);
			widgets.add(linkFactory.listCreate(request));
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
