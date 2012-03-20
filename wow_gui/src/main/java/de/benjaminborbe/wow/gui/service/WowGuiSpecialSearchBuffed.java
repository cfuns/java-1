package de.benjaminborbe.wow.gui.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class WowGuiSpecialSearchBuffed extends WowGuiSpecialSearchBase {

	@Inject
	public WowGuiSpecialSearchBuffed(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public String getName() {
		return "buffed";
	}

	@Override
	protected String getSearchUrl() {
		return "http://wowdata.buffed.de/?f=";
	}

}
