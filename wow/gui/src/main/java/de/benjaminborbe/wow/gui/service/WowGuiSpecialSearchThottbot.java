package de.benjaminborbe.wow.gui.service;

import de.benjaminborbe.tools.url.UrlUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class WowGuiSpecialSearchThottbot extends WowGuiSpecialSearchBase {

	private static final List<String> NAMES = Arrays.asList("thottbot");

	@Inject
	public WowGuiSpecialSearchThottbot(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	protected String getSearchUrl() {
		return "http://thottbot.com/search?q=";
	}

}
