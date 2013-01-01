package de.benjaminborbe.distributed.index.util;

import de.benjaminborbe.distributed.index.DistributedIndexConstants;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.dao.DistributedIndexEntryIdentifier;

public class DistributedIndexSearchResultImpl implements DistributedIndexSearchResult {

	private final Integer rating;

	private final String id;

	private final String space;

	public DistributedIndexSearchResultImpl(final Integer rating, final String space, final String id) {
		this.rating = rating;
		this.id = id;
		this.space = space;
	}

	public DistributedIndexSearchResultImpl(final Integer rating, final DistributedIndexEntryIdentifier distributedIndexEntryIdentifier) {
		this.rating = rating;
		final String entryId = distributedIndexEntryIdentifier.getId();
		final String[] parts = entryId.split(DistributedIndexConstants.SEPERATOR, 2);
		space = parts[0];
		id = parts[1];
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Integer getRating() {
		return rating;
	}

	@Override
	public String getSpace() {
		return space;
	}

}
