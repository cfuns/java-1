package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.search.api.DistributedSearchResult;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.api.DistributedSearchServiceException;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;

@Singleton
public class IndexServiceDistributed implements IndexService {

	private final DistributedSearchService distributedSearchService;

	@Inject
	public IndexServiceDistributed(final DistributedSearchService distributedSearchService) {
		this.distributedSearchService = distributedSearchService;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws IndexerServiceException {
		try {
			distributedSearchService.addToIndex(index, url, title, content);
		}
		catch (final DistributedSearchServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
		try {
			distributedSearchService.clear(indexName);
		}
		catch (final DistributedSearchServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		try {
			final List<IndexSearchResult> result = new ArrayList<IndexSearchResult>();
			for (final DistributedSearchResult r : distributedSearchService.search(index, searchQuery, limit)) {
				result.add(new IndexSearchResultImpl(r.getIndex(), r.getURL(), r.getTitle(), r.getContent()));
			}
			return result;
		}
		catch (final DistributedSearchServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws IndexerServiceException {
		try {
			distributedSearchService.removeFromIndex(index, url);
		}
		catch (final DistributedSearchServiceException e) {
			throw new IndexerServiceException(e);
		}
	}
}
