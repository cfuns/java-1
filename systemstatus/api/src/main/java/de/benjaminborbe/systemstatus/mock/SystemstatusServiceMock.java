package de.benjaminborbe.systemstatus.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;

@Singleton
public class SystemstatusServiceMock implements SystemstatusService {

	@Inject
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
