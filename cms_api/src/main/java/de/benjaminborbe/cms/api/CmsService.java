package de.benjaminborbe.cms.api;

public interface CmsService {

	public CmsPageIdentifier createPage(CmsPageDto cmsPageDto);

	public void updatePage(CmsPageDto cmsPageDto);

	public CmsPage getPage(CmsPageIdentifier cmsPageIdentifier);
}
