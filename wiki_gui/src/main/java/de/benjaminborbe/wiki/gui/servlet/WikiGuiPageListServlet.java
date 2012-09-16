package de.benjaminborbe.wiki.gui.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiPageNotFoundException;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.gui.WikiGuiConstants;

@Singleton
public class WikiGuiPageListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Wiki - Page-List";

	private final WikiService wikiService;

	private final UrlUtil urlUtil;

	private final Logger logger;

	@Inject
	public WikiGuiPageListServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final WikiService wikiService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, httpContextProvider, urlUtil);
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
			logger.debug("render " + getClass().getSimpleName());
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final WikiSpaceIdentifier wikiSpaceIdentifier = wikiService.getSpaceByName(request.getParameter(WikiGuiConstants.PARAMETER_SPACE_ID));

			// page list
			{
				final UlWidget ul = new UlWidget();
				for (final WikiPageIdentifier wikiPageIdentifier : wikiService.getPageIdentifiers(wikiSpaceIdentifier)) {
					final WikiPage page = wikiService.getPage(wikiPageIdentifier);
					ul.add(new LinkRelativWidget(urlUtil, request, "/wiki/page/show", new MapChain<String, String>().add(WikiGuiConstants.PARAMETER_PAGE_ID, page.getId().getId()).add(
							WikiGuiConstants.PARAMETER_SPACE_ID, wikiSpaceIdentifier.getId()), page.getTitle()));
				}
				widgets.add(ul);
				widgets.add(new LinkRelativWidget(urlUtil, request, "/wiki/page/create", new MapChain<String, String>().add(WikiGuiConstants.PARAMETER_SPACE_ID,
						wikiSpaceIdentifier.getId()), "add page"));
			}
			return widgets;
		}
		catch (final WikiServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final WikiPageNotFoundException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
