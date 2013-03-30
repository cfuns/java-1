package de.benjaminborbe.virt.core.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;

public class MapperVirtNetworkIdentifier implements Mapper<VirtNetworkIdentifier> {

	@Override
	public String toString(final VirtNetworkIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public VirtNetworkIdentifier fromString(final String value) {
		return value != null ? new VirtNetworkIdentifier(value) : null;
	}

}
