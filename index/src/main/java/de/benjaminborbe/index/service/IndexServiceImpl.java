package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.index.util.IndexServiceFactory;

@Singleton
public class IndexServiceImpl implements IndexService {

	private final IndexServiceFactory indexServiceFactory;

	@Inject
	public IndexServiceImpl(final IndexServiceFactory indexServiceFactory) {
		this.indexServiceFactory = indexServiceFactory;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws IndexerServiceException {
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			indexService.addToIndex(index, url, title, content);
		}
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			indexService.clear(indexName);
		}
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		for (final IndexService indexService : indexServiceFactory.getIndexServices()) {
			return indexService.search(index, searchQuery, limit);
		}
		throw new IndexerServiceException("at least one index must be enabled");
	}

}
