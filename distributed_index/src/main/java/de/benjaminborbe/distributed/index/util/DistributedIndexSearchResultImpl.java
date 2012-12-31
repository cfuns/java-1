package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;

public class DistributedIndexSearchResultImpl implements DistributedIndexSearchResult {

	private final Integer rating;

	private final DistributedIndexIdentifier id;

	public DistributedIndexSearchResultImpl(final Integer rating, final DistributedIndexIdentifier id) {
		this.rating = rating;
		this.id = id;
	}

	@Override
	public DistributedIndexIdentifier getId() {
		return id;
	}

	@Override
	public Integer getRating() {
		return rating;
	}

}
