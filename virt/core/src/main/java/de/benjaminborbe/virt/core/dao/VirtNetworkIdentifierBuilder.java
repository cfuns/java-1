package de.benjaminborbe.virt.core.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;

public class VirtNetworkIdentifierBuilder implements IdentifierBuilder<String, VirtNetworkIdentifier> {

	@Override
	public VirtNetworkIdentifier buildIdentifier(final String value) {
		return new VirtNetworkIdentifier(value);
	}

}
