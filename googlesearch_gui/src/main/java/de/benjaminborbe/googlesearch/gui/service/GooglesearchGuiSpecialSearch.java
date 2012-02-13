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
public class GooglesearchGuiSpecialSearch implements SearchSpecial {

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public GooglesearchGuiSpecialSearch(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public String getName() {
		return "g";
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		final StringWriter sw = new StringWriter();
		sw.append("http://www.google.de/search?sourceid=bb&ie=UTF-8&q=");
		sw.append(urlUtil.encode(searchQuery.replaceFirst("g: ", "")));
		response.sendRedirect(sw.toString());
	}

}
