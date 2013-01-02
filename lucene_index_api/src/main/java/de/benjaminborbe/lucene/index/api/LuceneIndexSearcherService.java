package de.benjaminborbe.lucene.index.api;

import java.util.List;

public interface LuceneIndexSearcherService {

	List<LuceneIndexSearchResult> search(String index, String searchQuery, int limit);
}
