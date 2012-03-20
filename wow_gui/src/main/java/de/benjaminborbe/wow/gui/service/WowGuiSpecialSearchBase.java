package de.benjaminborbe.wow.gui.service;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.url.UrlUtil;

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
		sw.append(urlUtil.encode(searchQuery.replaceFirst(getName() + ": ", "")));
		response.sendRedirect(sw.toString());
	}

}
