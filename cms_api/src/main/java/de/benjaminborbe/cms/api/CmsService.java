package de.benjaminborbe.cms.api;

public interface CmsService {

	public CmsPageIdentifier createPage();

	public void updatePage();

	public CmsPage getPage();
}
