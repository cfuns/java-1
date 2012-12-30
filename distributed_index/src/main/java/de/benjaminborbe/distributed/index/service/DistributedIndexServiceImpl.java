package de.benjaminborbe.distributed.index.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.index.api.DistributedIndexService;

@Singleton
public class DistributedIndexServiceImpl implements DistributedIndexService {

	private final Logger logger;

	@Inject
	public DistributedIndexServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
