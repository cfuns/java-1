package de.benjaminborbe.googlesearch.gui.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class GooglesearchGuiSpecialSearch implements SearchSpecial {

	private static final List<String> NAMES = Arrays.asList("g", "google");

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public GooglesearchGuiSpecialSearch(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public Collection<String> getNames() {
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
		sw.append("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=");
		sw.append(urlUtil.encode(term));
		return sw.toString();
	}
}
