package de.benjaminborbe.systemstatus.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.systemstatus.api.SystemstatusService;

@Singleton
public class SystemstatusServiceImpl implements SystemstatusService {

	private final Logger logger;

	@Inject
	public SystemstatusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
