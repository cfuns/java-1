package de.benjaminborbe.index.api;

import java.util.List;

public interface IndexSearcherService {

	List<IndexSearchResult> search(String index, String searchQuery);
}
