package de.benjaminborbe.bookmark.gui.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.bookmark.gui.widget.BookmarkCreateLink;
import de.benjaminborbe.bookmark.gui.widget.BookmarkListLink;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class BookmarkGuiSpecialSearch implements SearchSpecial {

	private static final List<String> NAMES = Arrays.asList("b");

	private final static String PARAMETER_SEARCH = "q";

	private final BookmarkService bookmarkService;

	private final SearchUtil searchUtil;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public BookmarkGuiSpecialSearch(final Logger logger, final BookmarkService bookmarkService, final SearchUtil searchUtil, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
		this.searchUtil = searchUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	public Collection<String> getNames() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final String searchQuery = request.getParameter(PARAMETER_SEARCH);
			final String[] words = searchUtil.buildSearchParts(searchQuery.substring(searchQuery.indexOf(":") + 1).trim());
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<Bookmark> bookmarks = bookmarkService.searchBookmarks(sessionIdentifier, words);
			if (bookmarks.size() > 0) {
				response.sendRedirect(bookmarks.get(0).getUrl());
			}
			else {
				final ListWidget widgets = new ListWidget();
				widgets.add(new H2Widget("Bookmarksearch"));
				widgets.add("no match");
				widgets.add(new BrWidget());
				widgets.add(new BookmarkCreateLink(request));
				widgets.add(" ");
				widgets.add(new BookmarkListLink(request));
				widgets.render(request, response, context);
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final BookmarkServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final LoginRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}
}
