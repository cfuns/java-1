package de.benjaminborbe.distributed.index.api;

public interface DistributedIndexSearchResultIterator {

	boolean hasNext() throws DistributedIndexServiceException;

	DistributedIndexSearchResult next() throws DistributedIndexServiceException;
}
