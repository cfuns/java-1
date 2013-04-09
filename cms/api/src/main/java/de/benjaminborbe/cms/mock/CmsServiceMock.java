package de.benjaminborbe.cms.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.cms.api.CmsPage;
import de.benjaminborbe.cms.api.CmsPageIdentifier;
import de.benjaminborbe.cms.api.CmsService;

@Singleton
public class CmsServiceMock implements CmsService {

	@Inject
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
