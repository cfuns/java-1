package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.List;

import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;

@Singleton
public class IndexServiceImpl implements IndexService {

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws IndexerServiceException {
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) {
		return null;
	}
}
