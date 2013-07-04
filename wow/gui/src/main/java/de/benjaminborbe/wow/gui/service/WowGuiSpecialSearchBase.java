package de.benjaminborbe.wow.gui.service;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.url.UrlUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

public abstract class WowGuiSpecialSearchBase implements SearchSpecial {

	private final static String PARAMETER_SEARCH = "q";

	private final UrlUtil urlUtil;

	@Inject
	public WowGuiSpecialSearchBase(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	protected abstract String getSearchUrl();

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String searchQuery = request.getParameter(PARAMETER_SEARCH);
		final StringWriter sw = new StringWriter();
		sw.append(getSearchUrl());
		sw.append(urlUtil.encode(searchQuery.substring(searchQuery.indexOf(":") + 1).trim()));
		response.sendRedirect(sw.toString());
	}

}
