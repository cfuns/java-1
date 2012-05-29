package de.benjaminborbe.googlesearch.gui.service;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class GooglesearchGuiMapSpecialSearch implements SearchSpecial {

	private static final String NAME = "maps";

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public GooglesearchGuiMapSpecialSearch(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		final StringWriter sw = new StringWriter();
		sw.append("https://maps.google.com/maps?hl=en&q=");
		sw.append(urlUtil.encode(searchQuery.replaceFirst(NAME + ": ", "")));
		response.sendRedirect(sw.toString());
	}

}
