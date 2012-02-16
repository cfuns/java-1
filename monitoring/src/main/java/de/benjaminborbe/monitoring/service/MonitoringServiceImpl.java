package de.benjaminborbe.monitoring.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.check.NodeCheckerCache;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.monitoring.check.SilentNodeRegistry;

@Singleton
public class MonitoringServiceImpl implements MonitoringService {

	private final Logger logger;

	private final RootNode rootNode;

	private final NodeCheckerCache nodeChecker;

	private final SilentNodeRegistry silentNodeRegistry;

	@Inject
	public MonitoringServiceImpl(final Logger logger, final RootNode rootNode, final NodeCheckerCache nodeChecker, final SilentNodeRegistry silentNodeRegistry) {
		this.logger = logger;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
		this.silentNodeRegistry = silentNodeRegistry;
	}

	@Override
	public Collection<CheckResult> checkRootNode() {
		logger.trace("checkRootNode");
		return nodeChecker.checkNode(rootNode);
	}

	@Override
	public Collection<CheckResult> checkRootNodeWithCache() {
		logger.trace("checkRootNodeWithCache");
		return nodeChecker.checkNodeWithCache(rootNode);
	}

	@Override
	public void silentCheck(final String checkName) {
		logger.debug("silentCheck");
		silentNodeRegistry.add(checkName);
	}

}
