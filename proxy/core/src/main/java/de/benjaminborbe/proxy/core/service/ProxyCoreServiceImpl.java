package de.benjaminborbe.proxy.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.proxy.api.ProxyService;
import org.slf4j.Logger;

@Singleton
public class ProxyCoreServiceImpl implements ProxyService {

	private final Logger logger;

	@Inject
	public ProxyCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
