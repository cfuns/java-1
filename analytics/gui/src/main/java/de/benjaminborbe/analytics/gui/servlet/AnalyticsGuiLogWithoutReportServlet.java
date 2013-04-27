package de.benjaminborbe.analytics.gui.servlet;

import com.google.common.collect.Lists;
import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiLinkFactory;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
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
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Singleton
public class AnalyticsGuiLogWithoutReportServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -2124329148879227334L;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final AnalyticsService analyticsService;

	private static final String TITLE = "Analytics - Log Without Report";

	private final AnalyticsGuiLinkFactory analyticsGuiLinkFactory;

	@Inject
	public AnalyticsGuiLogWithoutReportServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final CacheService cacheService,
		final AnalyticsService analyticsService,
		final AnalyticsGuiLinkFactory analyticsGuiLinkFactory
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.analyticsService = analyticsService;
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
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final List<String> list = Lists.newArrayList(analyticsService.getLogWithoutReport(sessionIdentifier));
			Collections.sort(list);

			if (list.isEmpty()) {
				widgets.add("nothing found");
			} else {
				final TableWidget table = new TableWidget();
				table.addClass("sortable");
				final TableHeadWidget head = new TableHeadWidget();
				head.addCell("Name").addCell("");
				table.setHead(head);
				for (final String name : list) {
					final TableRowWidget row = new TableRowWidget();
					row.addCell(name);
					row.addCell(analyticsGuiLinkFactory.addReport(request, name));
					table.addRow(row);
				}
				widgets.add(table);
			}

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
}
