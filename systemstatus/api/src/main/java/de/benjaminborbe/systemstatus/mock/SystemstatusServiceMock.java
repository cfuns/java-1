package de.benjaminborbe.systemstatus.mock;

import java.util.Collection;


import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;

public class SystemstatusServiceMock implements SystemstatusService {

	public SystemstatusServiceMock() {
	}

	@Override
	public Collection<SystemstatusPartition> getPartitions() {
		return null;
	}

	@Override
	public SystemstatusMemoryUsage getMemoryUsage() {
		return null;
	}
}
