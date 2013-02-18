package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.api.IteratorWithException;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.tools.iterator.IteratorCurrent;

public class DistributedIndexSearchResultIteratorCurrent extends IteratorCurrent<DistributedIndexSearchResult, DistributedIndexServiceException> implements
		DistributedIndexSearchResultIterator {

	public DistributedIndexSearchResultIteratorCurrent(final IteratorWithException<DistributedIndexSearchResult, DistributedIndexServiceException> iterator) {
		super(iterator);
	}

}
