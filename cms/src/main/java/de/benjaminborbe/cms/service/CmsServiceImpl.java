package de.benjaminborbe.cms.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cms.api.CmsService;

@Singleton
public class CmsServiceImpl implements CmsService {

	private final Logger logger;

	@Inject
	public CmsServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
