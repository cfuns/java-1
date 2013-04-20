package de.benjaminborbe.cms.mock;

import de.benjaminborbe.cms.api.CmsPage;
import de.benjaminborbe.cms.api.CmsPageIdentifier;
import de.benjaminborbe.cms.api.CmsService;

public class CmsServiceMock implements CmsService {

	public CmsServiceMock() {
	}

	@Override
	public CmsPageIdentifier createPage() {
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
