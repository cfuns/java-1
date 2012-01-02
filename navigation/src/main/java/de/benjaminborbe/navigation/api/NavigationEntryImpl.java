package de.benjaminborbe.navigation.api;

import java.net.URL;

public class NavigationEntryImpl implements NavigationEntry {

	private final URL url;

	private final String title;

	public NavigationEntryImpl(final String title, final URL url) {
		this.title = title;
		this.url = url;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public URL getURL() {
		return url;
	}

}
