package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class CssResourceWidget implements Widget {

	private final Collection<CssResource> cssResources;

	@Inject
	public CssResourceWidget(final Collection<CssResource> cssResources) {
		this.cssResources = cssResources;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		for (final CssResource cssResource : cssResources) {
			out.println("<link href=\"" + cssResource.getUrl() + "\" rel=\"stylesheet\" type=\"text/css\" />");
		}
	}
}
