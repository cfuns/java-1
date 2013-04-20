package de.benjaminborbe.search.gui.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class SearchGuiBugzillaSpecialSearch implements SearchSpecial {

	private static final List<String> NAMES = Arrays.asList("bugzilla", "et");

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public SearchGuiBugzillaSpecialSearch(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		response.sendRedirect(buildRedirect(searchQuery));
	}

	protected String buildRedirect(final String searchQuery) throws UnsupportedEncodingException {
		final String term = searchQuery.substring(searchQuery.indexOf(":") + 1).trim();
		final StringWriter sw = new StringWriter();
		sw.append("https://code.allianz24.de/az24/bugzilla/show_bug.cgi?id=");
		sw.append(urlUtil.encode(term));
		return sw.toString();
	}
}
