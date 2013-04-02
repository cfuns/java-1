package de.benjaminborbe.index.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.api.IndexerServiceException;
import de.benjaminborbe.lucene.index.api.LuceneIndexSearchResult;
import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.api.LuceneIndexServiceException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Singleton
public class IndexServiceLucene implements IndexService {

	private final LuceneIndexService luceneIndexService;

	@Inject
	public IndexServiceLucene(final LuceneIndexService luceneIndexService) {
		this.luceneIndexService = luceneIndexService;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content, final Calendar date) throws IndexerServiceException {
		try {
			luceneIndexService.addToIndex(index, url, title, content);
		} catch (final LuceneIndexServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
		try {
			luceneIndexService.clear(indexName);
		} catch (final LuceneIndexServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public List<IndexSearchResult> search(final String index, final String searchQuery, final int limit) throws IndexerServiceException {
		try {
			final List<IndexSearchResult> result = new ArrayList<>();
			for (final LuceneIndexSearchResult r : luceneIndexService.search(index, searchQuery, limit)) {
				result.add(new IndexSearchResultImpl(r.getIndex(), r.getURL(), r.getTitle(), r.getContent()));
			}
			return result;
		} catch (final LuceneIndexServiceException e) {
			throw new IndexerServiceException(e);
		}
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws IndexerServiceException {
		try {
			luceneIndexService.removeFromIndex(index, url);
		} catch (final LuceneIndexServiceException e) {
			throw new IndexerServiceException(e);
		}
	}
}
