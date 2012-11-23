package de.benjaminborbe.confluence.connector;

public class ConfluenceConnectorPage {

	private final String pageId;

	private final String url;

	private final String title;

	public ConfluenceConnectorPage(final String pageId, final String url, final String title) {
		this.pageId = pageId;
		this.url = url;
		this.title = title;
	}

	public String getPageId() {
		return pageId;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

}
