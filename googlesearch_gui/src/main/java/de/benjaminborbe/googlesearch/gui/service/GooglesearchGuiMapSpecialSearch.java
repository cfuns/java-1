package de.benjaminborbe.googlesearch.gui.service;

import java.io.IOException;
import java.io.StringWriter;
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
public class GooglesearchGuiMapSpecialSearch implements SearchSpecial {

	private static final List<String> NAMES = Arrays.asList("maps", "map");

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public GooglesearchGuiMapSpecialSearch(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public Collection<String> getNames() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		final StringWriter sw = new StringWriter();
		sw.append("https://maps.google.com/maps?hl=en&q=");
		sw.append(urlUtil.encode(searchQuery.substring(searchQuery.indexOf(":") + 1).trim()));
		response.sendRedirect(sw.toString());
	}

}
