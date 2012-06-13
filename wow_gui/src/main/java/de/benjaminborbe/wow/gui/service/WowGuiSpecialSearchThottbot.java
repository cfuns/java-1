package de.benjaminborbe.wow.gui.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class WowGuiSpecialSearchThottbot extends WowGuiSpecialSearchBase {

	private static final List<String> NAMES = Arrays.asList("thottbot");

	@Inject
	public WowGuiSpecialSearchThottbot(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public Collection<String> getNames() {
		return NAMES;
	}

	@Override
	protected String getSearchUrl() {
		return "http://thottbot.com/search?q=";
	}

}
