package de.benjaminborbe.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;

@Singleton
public class SearchServiceImpl implements SearchService {

	private final class SearchThreadRunner implements Runnable {

		private final SearchServiceComponent searchServiceComponent;

		private final ThreadResult<List<SearchResult>> threadResult;

		private final SessionIdentifier sessionIdentifier;

		private final String[] words;

		private final int maxResults;

		private SearchThreadRunner(final SearchServiceComponent searchServiceComponent, final ThreadResult<List<SearchResult>> threadResult, final SessionIdentifier sessionIdentifier, final String[] words, final int maxResults) {
			this.searchServiceComponent = searchServiceComponent;
			this.threadResult = threadResult;
			this.sessionIdentifier = sessionIdentifier;
			this.words = words;
			this.maxResults = maxResults;
		}

		@Override
		public void run() {
			try {
				final List<SearchResult> list = threadResult.get();
				list.addAll(searchServiceComponent.search(sessionIdentifier, words, maxResults));
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private final Logger logger;

	private final SearchServiceComponentRegistry searchServiceComponentRegistry;

	private final ThreadRunner threadRunner;

	@Inject
	public SearchServiceImpl(final Logger logger, final SearchServiceComponentRegistry searchServiceComponentRegistry, final ThreadRunner threadRunner) {
		this.logger = logger;
		this.searchServiceComponentRegistry = searchServiceComponentRegistry;
		this.threadRunner = threadRunner;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String[] words, final int maxResults) {
		logger.debug("search words: " + StringUtils.join(words, ","));

		final Collection<SearchServiceComponent> searchServiceComponents = searchServiceComponentRegistry.getAll();
		logger.debug("searchServiceComponents " + searchServiceComponents.size());

		final Collection<Thread> threads = new HashSet<Thread>();
		final Collection<ThreadResult<List<SearchResult>>> threadResults = new HashSet<ThreadResult<List<SearchResult>>>();

		for (final SearchServiceComponent searchServiceComponent : searchServiceComponents) {
			logger.debug("search in searchServiceComponent: " + searchServiceComponent.getClass().getSimpleName());

			final ThreadResult<List<SearchResult>> threadResult = new ThreadResult<List<SearchResult>>();
			threadResults.add(threadResult);
			threads.add(threadRunner.run("search", new SearchThreadRunner(searchServiceComponent, threadResult, sessionIdentifier, words, maxResults)));
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			}
			catch (final InterruptedException e) {
			}
		}

		final List<SearchResult> result = new ArrayList<SearchResult>();
		for (final ThreadResult<List<SearchResult>> threadResult : threadResults) {
			result.addAll(threadResult.get());
		}

		logger.debug("found " + result.size() + " results");
		return result;
	}

	@Override
	public List<String> getSearchComponentNames() {
		final List<String> result = new ArrayList<String>();
		for (final SearchServiceComponent cs : searchServiceComponentRegistry.getAll()) {
			result.add(cs.getClass().getName());
		}
		return result;
	}

}
