package de.benjaminborbe.wow.gui.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class WowGuiSpecialSearchBuffed extends WowGuiSpecialSearchBase {

	private static final List<String> NAMES = Arrays.asList("buffed");

	@Inject
	public WowGuiSpecialSearchBuffed(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	protected String getSearchUrl() {
		return "http://wowdata.buffed.de/?f=";
	}

}
