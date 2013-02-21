package de.benjaminborbe.distributed.search.gui.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.distributed.search.api.DistributedSearchResult;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.api.DistributedSearchServiceException;
import de.benjaminborbe.distributed.search.gui.DistributedSearchGuiConstants;
import de.benjaminborbe.distributed.search.gui.util.DistributedSearchGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class DistributedSearchGuiPageServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "DistributedSearch - Page";

	private final DistributedSearchService distributedSearchService;

	private final Logger logger;

	private final DistributedSearchGuiLinkFactory distributedSearchGuiLinkFactory;

	private final CalendarUtil calendarUtil;

	@Inject
	public DistributedSearchGuiPageServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final DistributedSearchService distributedSearchService,
			final CacheService cacheService,
			final DistributedSearchGuiLinkFactory distributedSearchGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.distributedSearchService = distributedSearchService;
		this.distributedSearchGuiLinkFactory = distributedSearchGuiLinkFactory;
		this.calendarUtil = calendarUtil;
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

			final String index = request.getParameter(DistributedSearchGuiConstants.PARAMETER_INDEX);
			final String url = request.getParameter(DistributedSearchGuiConstants.PARAMETER_URL);

			if (index != null && url != null) {
				final DistributedSearchResult page = distributedSearchService.getPage(index, url);
				if (page == null) {
					widgets.add("page not found");
				}
				else {
					widgets.add(new H2Widget("Index"));
					widgets.add(page.getIndex());
					widgets.add(new H2Widget("Url"));
					widgets.add(page.getURL());
					widgets.add(new H2Widget("Date"));
					widgets.add(asString(page.getDate()));
					widgets.add(new H2Widget("Added"));
					widgets.add(asString(page.getAdded()));
					widgets.add(new H2Widget("Update"));
					widgets.add(asString(page.getUpdated()));
					widgets.add(new H2Widget("Title"));
					widgets.add(page.getTitle());
					widgets.add(new H2Widget("Content"));
					widgets.add(page.getContent());
					widgets.add(new BrWidget());
					widgets.add(new BrWidget());

					final ListWidget options = new ListWidget();

					options.add(distributedSearchGuiLinkFactory.showIndex(request, page.getIndex(), page.getURL()));
					options.add(" ");
					options.add(distributedSearchGuiLinkFactory.rebuildPage(request, page.getIndex(), page.getURL()));

					widgets.add(options);
				}
			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputTextWidget(DistributedSearchGuiConstants.PARAMETER_INDEX).addLabel("Index:"));
			formWidget.addFormInputWidget(new FormInputTextWidget(DistributedSearchGuiConstants.PARAMETER_URL).addLabel("Url:"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("show"));
			widgets.add(formWidget);

			return widgets;
		}
		catch (final DistributedSearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private String asString(final Calendar calendar) {
		return calendar != null ? calendarUtil.toDateTimeString(calendar) : "-";
	}
}
