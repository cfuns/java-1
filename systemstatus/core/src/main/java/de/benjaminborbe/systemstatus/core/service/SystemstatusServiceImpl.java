package de.benjaminborbe.systemstatus.core.service;

import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.api.SystemstatusServiceException;
import de.benjaminborbe.systemstatus.core.util.SystemstatusMemoryUtil;
import de.benjaminborbe.systemstatus.core.util.SystemstatusPartitionUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class SystemstatusServiceImpl implements SystemstatusService {

	private final Logger logger;

	private final SystemstatusMemoryUtil systemstatusMemoryUtil;

	private final SystemstatusPartitionUtil systemstatusPartitionUtil;

	@Inject
	public SystemstatusServiceImpl(
		final Logger logger,
		final SystemstatusMemoryUtil systemstatusMemoryUtil,
		final SystemstatusPartitionUtil systemstatusPartitionUtil
	) {
		this.logger = logger;
		this.systemstatusMemoryUtil = systemstatusMemoryUtil;
		this.systemstatusPartitionUtil = systemstatusPartitionUtil;
	}

	@Override
	public Collection<SystemstatusPartition> getPartitions() throws SystemstatusServiceException {
		logger.debug("getPartitions");
		return systemstatusPartitionUtil.getPartitions();
	}

	@Override
	public SystemstatusMemoryUsage getMemoryUsage() throws SystemstatusServiceException {
		logger.debug("getMemoryUsage");
		return systemstatusMemoryUtil.getMemoryUsage();
	}

}
