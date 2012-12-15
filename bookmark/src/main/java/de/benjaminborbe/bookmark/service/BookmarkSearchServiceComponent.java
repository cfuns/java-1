package de.benjaminborbe.bookmark.service;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.Bookmark;
import de.benjaminborbe.bookmark.api.BookmarkMatch;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.util.ComparatorStringCaseInsensitive;

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
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final List<String> words) {
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();
		try {
			final List<BookmarkMatch> bookmarks = bookmarkService.searchBookmarks(sessionIdentifier, maxResults, words);
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
			logger.trace(e.getClass().getName(), e);
		}
		catch (final LoginRequiredException e) {
			logger.trace(e.getClass().getName(), e);
		}
		return results;
	}

	protected SearchResult mapBookmark(final BookmarkMatch bookmarkMatch) throws MalformedURLException {
		final Bookmark bookmark = bookmarkMatch.getBookmark();
		final StringWriter description = new StringWriter();
		final List<String> keywords = bookmark.getKeywords();
		if (keywords != null && !keywords.isEmpty()) {
			Collections.sort(keywords, new ComparatorStringCaseInsensitive());
			description.append("[");
			description.append(StringUtils.join(keywords, ","));
			description.append("]");
			if (bookmark.getDescription() != null && bookmark.getDescription().length() > 0) {
				description.append(" - ");
			}
		}
		description.append(bookmark.getDescription());
		return new SearchResultImpl(SEARCH_TYPE, bookmarkMatch.getMatchCounter(), bookmark.getName(), bookmark.getUrl(), description.toString());
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}
}
