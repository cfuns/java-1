package de.benjaminborbe.wow.gui.service;

import de.benjaminborbe.tools.url.UrlUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class WowGuiSpecialSearchWowhead extends WowGuiSpecialSearchBase {

	private static final List<String> NAMES = Arrays.asList("wowhead");

	@Inject
	public WowGuiSpecialSearchWowhead(final UrlUtil urlUtil) {
		super(urlUtil);
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	protected String getSearchUrl() {
		return "http://www.wowhead.com/search?q=";
	}

}
