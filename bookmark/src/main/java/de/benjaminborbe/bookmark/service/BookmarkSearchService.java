package de.benjaminborbe.bookmark.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;

@Singleton
public class BookmarkSearchService implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Bookmark";

	private final Logger logger;

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkSearchService(final Logger logger, final BookmarkService bookmarkService) {
		this.logger = logger;
		this.bookmarkService = bookmarkService;
	}

	@Override
	public List<SearchResult> search(final String queryString, final int maxResults) {
		logger.debug("search: queryString: " + queryString + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();
		final List<Bookmark> bookmarks = bookmarkService.searchBookmarks(queryString);
		final int max = Math.min(maxResults, bookmarks.size());
		for (int i = 0; i < max; ++i) {
			try {
				results.add(mapBookmark(bookmarks.get(i)));
			}
			catch (final MalformedURLException e) {
				logger.error("MalformedURLException", e);
			}
		}
		logger.debug("search found " + results.size() + " bookmarks");
		return results;
	}

	protected SearchResult mapBookmark(final Bookmark bookmark) throws MalformedURLException {
		final URL url = buildUrl(bookmark.getUrl());
		return new SearchResultImpl(SEARCH_TYPE, bookmark.getName(), url, bookmark.getDescription());
	}

	protected URL buildUrl(final String url) throws MalformedURLException {
		if (url != null && url.indexOf("/") == 0) {
			return new URL("http://bb" + url);
		}
		else {
			return new URL(url);
		}
	}
}
