package de.benjaminborbe.wiki.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiPageNotFoundException;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.gui.WikiGuiConstants;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Singleton
public class WikiGuiPageShowServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Wiki - Show";

	private final WikiService wikiService;

	private final UrlUtil urlUtil;

	private final Logger logger;

	@Inject
	public WikiGuiPageShowServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final WikiService wikiService,
		final AuthorizationService authorizationService,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.wikiService = wikiService;
		this.urlUtil = urlUtil;
		this.logger = logger;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException {
		try {
			final WikiPageIdentifier wikiPageIdentifier = wikiService.createPageIdentifier(request.getParameter(WikiGuiConstants.PARAMETER_PAGE_ID));
			final WikiPage page = wikiService.getPage(wikiPageIdentifier);
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(page.getTitle()));

			widgets.add(new HtmlContentWidget(wikiService.renderPage(wikiPageIdentifier)));
			widgets.add(new BrWidget());
			widgets.add(new LinkRelativWidget(urlUtil, request, "/" + WikiGuiConstants.NAME + "/" + WikiGuiConstants.WIKI_GUI_PAGE_EDIT_SERVLET_URL, new MapParameter().add(
				WikiGuiConstants.PARAMETER_PAGE_ID, wikiPageIdentifier.getId()), "edit page"));

			return widgets;
		} catch (final WikiServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final WikiPageNotFoundException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/" + WikiGuiConstants.NAME + WikiGuiConstants.URL_CSS_STYLE));
		return result;
	}
}
