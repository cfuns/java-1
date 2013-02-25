package de.benjaminborbe.cms.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cms.api.CmsPage;
import de.benjaminborbe.cms.api.CmsPageDto;
import de.benjaminborbe.cms.api.CmsPageIdentifier;
import de.benjaminborbe.cms.api.CmsService;

@Singleton
public class CmsServiceImpl implements CmsService {

	private final Logger logger;

	@Inject
	public CmsServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public CmsPageIdentifier createPage(final CmsPageDto cmsPageDto) {
		logger.trace("createPage");
		return null;
	}

	@Override
	public void updatePage(CmsPageDto cmsPageDto) {
	}

	@Override
	public CmsPage getPage(CmsPageIdentifier cmsPageIdentifier) {
		return null;
	}

}
