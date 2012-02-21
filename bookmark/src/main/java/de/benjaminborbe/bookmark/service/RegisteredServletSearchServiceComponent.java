package de.benjaminborbe.bookmark.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;

@Singleton
public class RegisteredServletSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Servlet";

	private final Logger logger;

	private final ServletPathRegistry servletPathRegistry;

	@Inject
	public RegisteredServletSearchServiceComponent(final Logger logger, final ServletPathRegistry servletPathRegistry) {
		this.logger = logger;
		this.servletPathRegistry = servletPathRegistry;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String[] words, final int maxResults) {
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<SearchResult>();

		for (final String path : servletPathRegistry.getAll()) {
			for (final String word : words) {
				boolean match = false;
				if (path.contains(word)) {
					match = true;
				}
				if (match) {
					try {
						results.add(buildResult(path));
					}
					catch (final MalformedURLException e) {
					}
				}
			}
		}

		return results;
	}

	private SearchResult buildResult(final String path) throws MalformedURLException {
		final URL url = buildUrl(path);
		return new SearchResultImpl(SEARCH_TYPE, path, url, path);
	}

	private URL buildUrl(final String url) throws MalformedURLException {
		return new URL("http://bb/bb" + url);
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}
}
