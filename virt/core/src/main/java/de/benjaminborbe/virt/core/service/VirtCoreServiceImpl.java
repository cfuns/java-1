package de.benjaminborbe.virt.core.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.virt.api.VirtService;

@Singleton
public class VirtCoreServiceImpl implements VirtService {

	private final Logger logger;

	@Inject
	public VirtCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(long value) {
		logger.trace("execute");
		return value * 2;
	}

}
