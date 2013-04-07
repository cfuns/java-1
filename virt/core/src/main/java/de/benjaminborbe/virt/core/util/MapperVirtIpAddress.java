package de.benjaminborbe.virt.core.util;

import com.google.inject.Inject;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.virt.api.VirtIpAddress;

public class MapperVirtIpAddress implements Mapper<VirtIpAddress> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperVirtIpAddress(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public VirtIpAddress fromString(final String string) throws MapException {
		try {
			if (string != null) {
				final String[] parts = string.split("\\.");
				if (parts.length != 4) {
					throw new MapException("parse " + string + " failed");
				}
				return new VirtIpAddress(
					parseUtil.parseInt(parts[0]),
					parseUtil.parseInt(parts[1]),
					parseUtil.parseInt(parts[2]),
					parseUtil.parseInt(parts[3])
				);

			} else {
				return null;
			}
		} catch (ParseException e) {
			throw new MapException("parse " + string + " failed", e);
		}
	}

	@Override
	public String toString(final VirtIpAddress object) throws MapException {
		return object != null ? String.valueOf(object) : null;
	}
}
