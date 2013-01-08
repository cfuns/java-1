package de.benjaminborbe.distributed.search.mock;

import java.net.URL;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.search.api.DistributedSearchResult;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.api.DistributedSearchServiceException;

@Singleton
public class DistributedSearchServiceMock implements DistributedSearchService {

	@Inject
	public DistributedSearchServiceMock() {
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws DistributedSearchServiceException {
	}

	@Override
	public void clear(final String indexName) throws DistributedSearchServiceException {
	}

	@Override
	public List<DistributedSearchResult> search(final String index, final String searchQuery, final int limit) throws DistributedSearchServiceException {
		return null;
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws DistributedSearchServiceException {
	}

	@Override
	public void rebuildIndex(final String index) throws DistributedSearchServiceException {
	}

	@Override
	public DistributedSearchResult getPage(final String index, final String url) throws DistributedSearchServiceException {
		return null;
	}

}
