package de.benjaminborbe.systemstatus.api;

import java.util.Collection;

public interface SystemstatusService {

	Collection<SystemstatusPartition> getPartitions() throws SystemstatusServiceException;

	SystemstatusMemoryUsage getMemoryUsage() throws SystemstatusServiceException;
}
