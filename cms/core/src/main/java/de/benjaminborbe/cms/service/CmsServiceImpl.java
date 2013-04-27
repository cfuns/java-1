package de.benjaminborbe.cms.service;

import de.benjaminborbe.cms.api.CmsPage;
import de.benjaminborbe.cms.api.CmsPageIdentifier;
import de.benjaminborbe.cms.api.CmsService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CmsServiceImpl implements CmsService {

	private final Logger logger;

	@Inject
	public CmsServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public CmsPageIdentifier createPage() {
		logger.trace("createPage");
		return null;
	}

	@Override
	public void updatePage() {
	}

	@Override
	public CmsPage getPage() {
		return null;
	}

}
