package de.benjaminborbe.confluence.api;

import de.benjaminborbe.api.IdentifierBase;

public class ConfluencePageIdentifier extends IdentifierBase<String> {

	public ConfluencePageIdentifier(final String id) {
		super(id);
	}

	public ConfluencePageIdentifier(final ConfluenceInstanceIdentifier instanceId, final String indexName, final String pageId) {
		this(instanceId.getId() + "-" + indexName + "-" + pageId);
	}
}
