package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;

public class DistributedIndexSearchResultIteratorCurrent implements DistributedIndexSearchResultIterator {

	private final DistributedIndexSearchResultIterator distributedIndexSearchResultIterator;

	private DistributedIndexSearchResult current;

	public DistributedIndexSearchResultIteratorCurrent(final DistributedIndexSearchResultIterator distributedIndexSearchResultIterator) {
		this.distributedIndexSearchResultIterator = distributedIndexSearchResultIterator;
	}

	@Override
	public boolean hasNext() throws DistributedIndexServiceException {
		return distributedIndexSearchResultIterator.hasNext();
	}

	@Override
	public DistributedIndexSearchResult next() throws DistributedIndexServiceException {
		return current = distributedIndexSearchResultIterator.next();
	}

	public DistributedIndexSearchResult getCurrent() {
		return current;
	}

	public void setCurrent(final DistributedIndexSearchResult current) {
		this.current = current;
	}

}
