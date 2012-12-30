package de.benjaminborbe.distributed.index.api;

import java.util.Collection;
import java.util.Map;

public interface DistributedIndexService {

	void add(DistributedIndexIdentifier id, Map<String, Integer> data) throws DistributedIndexServiceException;

	void remove(DistributedIndexIdentifier id) throws DistributedIndexServiceException;

	DistributedIndexSearchResultIterator search(Collection<String> words) throws DistributedIndexServiceException;
}
