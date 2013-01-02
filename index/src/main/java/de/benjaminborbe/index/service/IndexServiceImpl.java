package de.benjaminborbe.index.service;

import java.net.URL;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.index.config.IndexConfig;

@Singleton
public class IndexServiceImpl implements IndexService {

	private final IndexServiceDistributed indexServiceDistributed;

	private final IndexServiceLucene indexServiceLucene;

	private final IndexConfig indexConfig;

	@Inject
	public IndexServiceImpl(final IndexConfig indexConfig, final IndexServiceDistributed indexServiceDistributed, final IndexServiceLucene indexServiceLucene) {
		this.indexConfig = indexConfig;
		this.indexServiceDistributed = indexServiceDistributed;
		this.indexServiceLucene = indexServiceLucene;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws IndexerServiceException {
		indexServiceDistributed.addToIndex(index, url, title, content);
		indexServiceLucene.addToIndex(index, url, title, content);
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
		indexServiceDistributed.clear(indexName);
		indexServiceLucene.clear(indexName);
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		return getService().search(index, searchQuery, limit);
	}

	public IndexService getService() {
		if (indexConfig.getDistributedSearchEnabled()) {
			return indexServiceDistributed;
		}
		else {
			return indexServiceLucene;
		}
	}
}
