package de.benjaminborbe.websearch.gui.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.api.Page;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class WebsearchGuiListPagesServlet extends WebsiteHtmlServlet {

	private final class PageComparator extends ComparatorBase<Page, String> {

		@Override
		public String getValue(final Page o) {
			return o.getUrl() != null ? o.getUrl().toExternalForm() : null;
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Websearch - List Pages";

	private final WebsearchService websearchService;

	private final DateUtil dateUtil;

	private final UrlUtil urlUtil;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public WebsearchGuiListPagesServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final WebsearchService websearchService,
			final DateUtil dateUtil,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.websearchService = websearchService;
		this.dateUtil = dateUtil;
		this.urlUtil = urlUtil;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			for (final Page page : sortPages(websearchService.getPages(sessionIdentifier))) {
				ul.add(buildPageWidget(page, request));
			}
			widgets.add(ul);
			return widgets;
		}
		catch (final WebsearchServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	protected List<Page> sortPages(final Collection<Page> pages) {
		final List<Page> result = new ArrayList<Page>(pages);
		Collections.sort(result, new PageComparator());
		return result;
	}

	protected Widget buildPageWidget(final Page page, final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		final ListWidget widgets = new ListWidget();
		final URL url = page.getUrl();
		widgets.add(new LinkWidget(url, url.toExternalForm()));
		final StringWriter sw = new StringWriter();
		sw.append(" ");
		if (page.getLastVisit() != null) {
			sw.append(dateUtil.dateTimeString(page.getLastVisit()));
		}
		else {
			sw.append("-");
		}
		sw.append(" ");
		widgets.add(sw.toString());
		widgets.add(new LinkRelativWidget(urlUtil, request, "/websearch/expire", new MapParameter().add("url", url.toExternalForm()), "expire "));
		return widgets;
	}
}
