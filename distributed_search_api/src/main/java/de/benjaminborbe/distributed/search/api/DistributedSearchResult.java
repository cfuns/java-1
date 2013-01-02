package de.benjaminborbe.distributed.search.api;

public interface DistributedSearchResult {

	String getIndex();

	String getTitle();

	String getContent();

	String getURL();
}
