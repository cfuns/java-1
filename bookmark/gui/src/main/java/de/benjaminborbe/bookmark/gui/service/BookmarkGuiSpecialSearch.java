package de.benjaminborbe.bookmark.gui.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.bookmark.api.BookmarkMatch;
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

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class BookmarkGuiSpecialSearch implements SearchSpecial {

	private static final List<String> NAMES = Arrays.asList("b");

	private final static String PARAMETER_SEARCH = "q";

	private final BookmarkService bookmarkService;

	private final SearchUtil searchUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public BookmarkGuiSpecialSearch(final BookmarkService bookmarkService, final SearchUtil searchUtil, final AuthenticationService authenticationService) {
		this.bookmarkService = bookmarkService;
		this.searchUtil = searchUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final String searchQuery = request.getParameter(PARAMETER_SEARCH);
			final List<String> words = searchUtil.buildSearchParts(searchQuery.substring(searchQuery.indexOf(":") + 1).trim());
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<BookmarkMatch> bookmarks = bookmarkService.searchBookmarks(sessionIdentifier, 1, words);
			if (bookmarks.size() > 0) {
				response.sendRedirect(bookmarks.get(0).getBookmark().getUrl());
			} else {
				final ListWidget widgets = new ListWidget();
				widgets.add(new H2Widget("Bookmarksearch"));
				widgets.add("no match");
				widgets.add(new BrWidget());
				widgets.add(new BookmarkCreateLink(request));
				widgets.add(" ");
				widgets.add(new BookmarkListLink(request));
				widgets.add(new BrWidget());
				widgets.render(request, response, context);
			}
		} catch (final AuthenticationServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (PermissionDeniedException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (BookmarkServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (LoginRequiredException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}
}
