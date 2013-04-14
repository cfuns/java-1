package de.benjaminborbe.dns.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.dns.api.DnsService;
import org.slf4j.Logger;

@Singleton
public class DnsCoreServiceImpl implements DnsService {

	private final Logger logger;

	@Inject
	public DnsCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
