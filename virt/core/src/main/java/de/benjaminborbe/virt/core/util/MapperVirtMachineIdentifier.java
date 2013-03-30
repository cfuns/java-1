package de.benjaminborbe.virt.core.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.virt.api.VirtMachineIdentifier;

public class MapperVirtMachineIdentifier implements Mapper<VirtMachineIdentifier> {

	@Override
	public String toString(final VirtMachineIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public VirtMachineIdentifier fromString(final String value) {
		return value != null ? new VirtMachineIdentifier(value) : null;
	}

}
