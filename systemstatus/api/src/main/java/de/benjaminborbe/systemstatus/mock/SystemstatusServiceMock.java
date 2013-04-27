package de.benjaminborbe.systemstatus.mock;

import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;

import java.util.Collection;

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
