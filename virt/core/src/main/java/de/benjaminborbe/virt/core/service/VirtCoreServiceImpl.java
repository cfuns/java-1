package de.benjaminborbe.virt.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.api.VirtServiceException;
import org.slf4j.Logger;

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

	@Override
	public VirtNetworkIdentifier createVirtNetworkIdentifier(final String id) throws VirtServiceException {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtNetworkIdentifier(id);
		}
	}

}
