package de.benjaminborbe.search.api;

import java.util.List;

public interface SearchService extends SearchServiceComponent {

	List<String> getSearchComponentNames();
}
