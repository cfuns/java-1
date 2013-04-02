package de.benjaminborbe.index.mock;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;

import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class IndexServiceMock implements IndexService {

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content, final Calendar date) {
	}

	@Override
	public void clear(final String indexName) {
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		return null;
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws IndexerServiceException {
	}

}
