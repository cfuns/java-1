package de.benjaminborbe.confluence.gui.servlet;

import com.google.common.collect.Lists;
import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.gui.util.ConfluenceGuiLinkFactory;
import de.benjaminborbe.confluence.gui.util.ConfluenceInstanceComparator;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.table.TableHeadWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		final UrlUtil urlUtil,
		final ConfluenceGuiLinkFactory linkFactory,
		final ConfluenceService confluenceService,
		final AuthorizationService authorizationService,
		final ConfluenceInstanceComparator confluenceInstanceComparator,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
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
			final List<ConfluenceInstance> confluenceInstances = Lists.newArrayList(confluenceService.getConfluenceInstances(sessionIdentifier));
			Collections.sort(confluenceInstances, confluenceInstanceComparator);

			final TableWidget table = new TableWidget();
			table.addClass("sortable");
			final TableHeadWidget head = new TableHeadWidget();
			head.addCell("Url").addCell("Perm").addCell("User").addCell("Active").addCell("");
			table.setHead(head);
			for (final ConfluenceInstance confluenceInstance : confluenceInstances) {
				final TableRowWidget row = new TableRowWidget();
				row.addCell(confluenceInstance.getUrl());
				row.addCell(Boolean.TRUE.equals(confluenceInstance.getShared()) ? "shared" : "private");
				row.addCell(confluenceInstance.getUsername());
				row.addCell(String.valueOf(confluenceInstance.getActivated()));
				final ListWidget options = new ListWidget();
				options.add(linkFactory.updateInstance(request, confluenceInstance.getId()));
				options.add(" ");
				options.add(linkFactory.refreshPage(request, confluenceInstance.getId()));
				options.add(" ");
				options.add(linkFactory.deleteInstance(request, confluenceInstance.getId()));
				row.addCell(options);
				row.addClass(Boolean.TRUE.equals(confluenceInstance.getActivated()) ? "confluenceInstanceActive" : "confluenceInstanceInactive");
				table.addRow(row);
			}
			widgets.add(table);

			final ListWidget links = new ListWidget();
			links.add(linkFactory.createInstance(request));
			links.add(" ");
			links.add(linkFactory.refreshIndex(request));
			links.add(" ");
			links.add(linkFactory.expireAll(request));
			widgets.add(links);
			return widgets;
		} catch (final ConfluenceServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}
}
