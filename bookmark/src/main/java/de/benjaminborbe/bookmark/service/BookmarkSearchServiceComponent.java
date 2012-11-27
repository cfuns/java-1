package de.benjaminborbe.bookmark.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;

@Singleton
public class BookmarkSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Bookmark";

	private final Logger logger;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkSearchServiceComponent(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final String[] words, final int maxResults) {
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();
		try {
			final List<Bookmark> bookmarks = bookmarkService.searchBookmarks(sessionIdentifier, words);
			final int max = Math.min(maxResults, bookmarks.size());
			for (int i = 0; i < max; ++i) {
				try {
					results.add(mapBookmark(bookmarks.get(i)));
				}
				catch (final MalformedURLException e) {
					logger.error("MalformedURLException", e);
				}
			}
			logger.trace("search found " + results.size() + " bookmarks");
		}
		catch (final BookmarkServiceException e) {
			logger.trace("BookmarkServiceException", e);
		}
		return results;
	}

	protected SearchResult mapBookmark(final Bookmark bookmark) throws MalformedURLException {
		return new SearchResultImpl(SEARCH_TYPE, bookmark.getName(), bookmark.getUrl(), bookmark.getDescription());
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}
}
