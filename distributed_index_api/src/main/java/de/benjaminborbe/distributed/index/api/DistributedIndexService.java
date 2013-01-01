package de.benjaminborbe.distributed.index.api;

import java.util.Collection;
import java.util.Map;

public interface DistributedIndexService {

	void add(DistributedIndexIdentifier index, DistributedIndexPageIdentifier page, Map<String, Integer> data) throws DistributedIndexServiceException;

	void remove(DistributedIndexIdentifier index, DistributedIndexPageIdentifier page) throws DistributedIndexServiceException;

	DistributedIndexSearchResultIterator search(DistributedIndexIdentifier index, Collection<String> words) throws DistributedIndexServiceException;
}
