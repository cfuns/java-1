package de.benjaminborbe.wow.gui.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class WowGuiSpecialSearchThottbot extends WowGuiSpecialSearchBase {

	@Inject
	public WowGuiSpecialSearchThottbot(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public String getName() {
		return "thottbot";
	}

	@Override
	protected String getSearchUrl() {
		return "http://thottbot.com/search?q=";
	}

}
