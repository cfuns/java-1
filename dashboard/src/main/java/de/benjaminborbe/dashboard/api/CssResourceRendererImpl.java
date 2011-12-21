package de.benjaminborbe.dashboard.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CssResourceRendererImpl implements CssResourceRenderer {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response,
			final Collection<CssResource> cssResources) throws IOException {
		final PrintWriter out = response.getWriter();
		for (final CssResource cssResource : cssResources) {
			out.println("<link href=\"" + cssResource.getUrl() + "\" rel=\"stylesheet\" type=\"text/css\" />");
		}

	}
}
