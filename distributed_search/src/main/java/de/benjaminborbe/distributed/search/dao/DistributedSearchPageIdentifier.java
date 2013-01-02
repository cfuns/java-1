package de.benjaminborbe.distributed.search.dao;

import de.benjaminborbe.api.IdentifierBase;
import de.benjaminborbe.distributed.search.DistributedSearchConstants;

public class DistributedSearchPageIdentifier extends IdentifierBase<String> {

	public DistributedSearchPageIdentifier(final String id) {
		super(id);
	}

	public DistributedSearchPageIdentifier(final String index, final String page) {
		this(index + DistributedSearchConstants.SEPERATOR + page);
	}

	public String getIndex() {
		final String[] parts = getId().split(DistributedSearchConstants.SEPERATOR);
		return parts[0];
	}

	public String getPageId() {
		final String[] parts = getId().split(DistributedSearchConstants.SEPERATOR);
		return parts[1];
	}
}
