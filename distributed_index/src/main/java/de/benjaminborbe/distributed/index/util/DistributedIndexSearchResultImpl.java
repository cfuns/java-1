package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;

public class DistributedIndexSearchResultImpl implements DistributedIndexSearchResult {

	private final Integer rating;

	private final DistributedIndexPageIdentifier id;

	public DistributedIndexSearchResultImpl(final Integer rating, final DistributedIndexPageIdentifier id) {
		this.rating = rating;
		this.id = id;
	}

	@Override
	public DistributedIndexPageIdentifier getId() {
		return id;
	}

	@Override
	public Integer getRating() {
		return rating;
	}

}
