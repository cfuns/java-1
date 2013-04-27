package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectWidget implements Widget {

	private final String target;

	public RedirectWidget(final String target) {
		this.target = target;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String s = "://";
		final String[] parts = target.split(s, 2);
		if (parts.length == 2) {
			response.sendRedirect(parts[0] + s + parts[1].replaceAll("//", "/"));
		} else {
			response.sendRedirect(target.replaceAll("//", "/"));
		}
	}
}
