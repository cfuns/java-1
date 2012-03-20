package de.benjaminborbe.wow.gui.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class WowGuiSpecialSearchWowhead extends WowGuiSpecialSearchBase {

	@Inject
	public WowGuiSpecialSearchWowhead(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public String getName() {
		return "wowhead";
	}

	@Override
	protected String getSearchUrl() {
		return "http://www.wowhead.com/search?q=";
	}

}
