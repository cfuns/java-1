package de.benjaminborbe.monitoring.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.check.NodeCheckerCache;
import de.benjaminborbe.monitoring.check.RootNode;

@Singleton
public class MonitoringServiceImpl implements MonitoringService {

	private final Logger logger;

	private final RootNode rootNode;

	private final NodeCheckerCache nodeChecker;

	@Inject
	public MonitoringServiceImpl(final Logger logger, final RootNode rootNode, final NodeCheckerCache nodeChecker) {
		this.logger = logger;
		this.rootNode = rootNode;
		this.nodeChecker = nodeChecker;
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

}
