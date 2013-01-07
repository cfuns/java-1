package de.benjaminborbe.confluence.gui.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.gui.util.ConfluenceGuiLinkFactory;
import de.benjaminborbe.confluence.gui.util.ConfluenceInstanceComparator;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class ConfluenceGuiInstanceListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Confluence - Instances";

	private final ConfluenceService confluenceService;

	private final Logger logger;

	private final ConfluenceGuiLinkFactory linkFactory;

	private final AuthenticationService authenticationService;

	private final ConfluenceInstanceComparator confluenceInstanceComparator;

	@Inject
	public ConfluenceGuiInstanceListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final ConfluenceGuiLinkFactory linkFactory,
			final ConfluenceService confluenceService,
			final AuthorizationService authorizationService,
			final ConfluenceInstanceComparator confluenceInstanceComparator) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.linkFactory = linkFactory;
		this.confluenceService = confluenceService;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.confluenceInstanceComparator = confluenceInstanceComparator;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(TITLE));
			final UlWidget ul = new UlWidget();
			final List<ConfluenceInstance> confluenceInstances = Lists.newArrayList(confluenceService.getConfluenceInstances(sessionIdentifier));
			Collections.sort(confluenceInstances, confluenceInstanceComparator);
			for (final ConfluenceInstance confluenceInstance : confluenceInstances) {
				final ListWidget list = new ListWidget();
				list.add(confluenceInstance.getUsername());
				list.add(" @ ");
				list.add(confluenceInstance.getUrl());
				list.add(" ");
				list.add(linkFactory.updateInstance(request, confluenceInstance.getId()));
				list.add(" ");
				list.add(linkFactory.deleteInstance(request, confluenceInstance.getId()));
				ul.add(new SpanWidget(list).addClass(Boolean.TRUE.equals(confluenceInstance.getActivated()) ? "confluenceInstanceActive" : "confluenceInstanceInactive"));
			}
			widgets.add(ul);

			final ListWidget links = new ListWidget();
			links.add(linkFactory.createInstance(request));
			links.add(" ");
			links.add(linkFactory.refreshIndex(request));
			links.add(" ");
			links.add(linkFactory.expireAll(request));
			widgets.add(links);
			return widgets;
		}
		catch (final ConfluenceServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}
}
