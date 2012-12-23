package de.benjaminborbe.search.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchService;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.search.dao.SearchQueryHistoryBean;
import de.benjaminborbe.search.dao.SearchQueryHistoryDao;
import de.benjaminborbe.search.dao.SearchQueryHistoryIdentifier;
import de.benjaminborbe.search.util.SearchServiceComponentRegistry;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;

@Singleton
public class SearchServiceImpl implements SearchService {

	private final class SearchQueryHistroryRunnable implements Runnable {

		private final String query;

		private SearchQueryHistroryRunnable(final String query) {
			this.query = query;
		}

		@Override
		public void run() {
			try {
				final SearchQueryHistoryBean searchQueryHistory = searchQueryHistoryDao.create();
				searchQueryHistory.setId(new SearchQueryHistoryIdentifier(idGeneratorUUID.nextId()));
				searchQueryHistory.setQuery(query);
				searchQueryHistoryDao.save(searchQueryHistory);
			}
			catch (final Exception e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

	private final class SearchThreadRunner implements Runnable {

		private final SearchServiceComponent searchServiceComponent;

		private final ThreadResult<List<SearchResult>> threadResult;

		private final SessionIdentifier sessionIdentifier;

		private final int maxResults;

		private final String query;

		private SearchThreadRunner(
				final SearchServiceComponent searchServiceComponent,
				final ThreadResult<List<SearchResult>> threadResult,
				final SessionIdentifier sessionIdentifier,
				final String query,
				final int maxResults) {
			this.searchServiceComponent = searchServiceComponent;
			this.threadResult = threadResult;
			this.sessionIdentifier = sessionIdentifier;
			this.maxResults = maxResults;
			this.query = query;
		}

		@Override
		public void run() {
			try {
				threadResult.set(searchServiceComponent.search(sessionIdentifier, query, maxResults));
			}
			catch (final Exception e) {
				logger.error(e.getClass().getSimpleName(), e);
			}
		}
	}

	private final Logger logger;

	private final SearchServiceComponentRegistry searchServiceComponentRegistry;

	private final ThreadRunner threadRunner;

	private final SearchServiceSearchResultComparator searchServiceComponentComparator;

	private final SearchQueryHistoryDao searchQueryHistoryDao;

	private final IdGeneratorUUID idGeneratorUUID;

	@Inject
	public SearchServiceImpl(
			final Logger logger,
			final SearchQueryHistoryDao searchQueryHistoryDao,
			final IdGeneratorUUID idGeneratorUUID,
			final SearchServiceComponentRegistry searchServiceComponentRegistry,
			final ThreadRunner threadRunner,
			final SearchServiceSearchResultComparator searchServiceComponentComparator) {
		this.logger = logger;
		this.searchQueryHistoryDao = searchQueryHistoryDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.searchServiceComponentRegistry = searchServiceComponentRegistry;
		this.threadRunner = threadRunner;
		this.searchServiceComponentComparator = searchServiceComponentComparator;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {

		createHistory(query);

		// search in one component
		{
			final int pos = query.indexOf(':');
			if (pos != -1) {
				final String name = query.substring(0, pos - 1);
				final SearchServiceComponent search = searchServiceComponentRegistry.get(name);
				if (search != null) {
					final List<SearchServiceComponent> searchServiceComponents = Arrays.asList(search);
					return search(searchServiceComponents, query.substring(pos), sessionIdentifier, maxResults);
				}
			}
		}

		final List<SearchServiceComponent> searchServiceComponents = new ArrayList<SearchServiceComponent>(searchServiceComponentRegistry.getAll());
		logger.trace("searchServiceComponents " + searchServiceComponents.size());
		return search(searchServiceComponents, query, sessionIdentifier, maxResults);
	}

	private List<SearchResult> search(final List<SearchServiceComponent> searchServiceComponents, final String query, final SessionIdentifier sessionIdentifier, final int maxResults) {

		final List<Thread> threads = new ArrayList<Thread>();
		final List<ThreadResult<List<SearchResult>>> threadResults = new ArrayList<ThreadResult<List<SearchResult>>>();

		for (final SearchServiceComponent searchServiceComponent : searchServiceComponents) {
			logger.trace("search in searchServiceComponent: " + searchServiceComponent.getClass().getSimpleName());

			final ThreadResult<List<SearchResult>> threadResult = new ThreadResult<List<SearchResult>>();
			threadResults.add(threadResult);
			threads.add(threadRunner.run("search", new SearchThreadRunner(searchServiceComponent, threadResult, sessionIdentifier, query, maxResults)));
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
			final List<SearchResult> list = threadResult.get();
			if (list != null) {
				result.addAll(list);
			}
		}
		Collections.sort(result, searchServiceComponentComparator);
		logger.trace("found " + result.size() + " results");
		return result;
	}

	private void createHistory(final String query) {
		threadRunner.run("searchQueryHistory", new SearchQueryHistroryRunnable(query));
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
