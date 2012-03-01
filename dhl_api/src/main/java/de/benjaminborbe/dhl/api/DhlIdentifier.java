package de.benjaminborbe.dhl.api;

import de.benjaminborbe.api.IdentifierBase;

public class DhlIdentifier extends IdentifierBase<Long> {

	private final Long zip;

	public DhlIdentifier(final Long id, final Long zip) {
		super(id);
		this.zip = zip;
	}

	public Long getZip() {
		return zip;
	}

}
