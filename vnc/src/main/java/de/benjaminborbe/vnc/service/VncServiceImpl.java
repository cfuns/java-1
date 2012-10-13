package de.benjaminborbe.vnc.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncService;

@Singleton
public class VncServiceImpl implements VncService {

	private final Logger logger;

	@Inject
	public VncServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
