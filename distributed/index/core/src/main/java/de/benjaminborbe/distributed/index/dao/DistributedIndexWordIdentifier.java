package de.benjaminborbe.distributed.index.dao;

import de.benjaminborbe.api.IdentifierBase;
import de.benjaminborbe.distributed.index.DistributedIndexConstants;

public class DistributedIndexWordIdentifier extends IdentifierBase<String> {

	public DistributedIndexWordIdentifier(final String id) {
		super(id);
	}

	public DistributedIndexWordIdentifier(final String index, final String word) {
		this(index + DistributedIndexConstants.SEPERATOR + word);
	}

	public String getIndex() {
		final String[] parts = getId().split(DistributedIndexConstants.SEPERATOR);
		return parts[0];
	}
}
