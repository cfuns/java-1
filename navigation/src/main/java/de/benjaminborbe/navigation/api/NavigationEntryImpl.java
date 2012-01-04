package de.benjaminborbe.navigation.api;

public class NavigationEntryImpl implements NavigationEntry {

	private final String url;

	private final String title;

	public NavigationEntryImpl(final String title, final String url) {
		this.title = title;
		this.url = url;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getURL() {
		return url;
	}

}
