package de.benjaminborbe.website.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class RedirectWidget implements Widget {

	private final String target;

	public RedirectWidget(final String target) {
		this.target = target;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		response.sendRedirect(target.replaceAll("//", "/"));
	}

}
