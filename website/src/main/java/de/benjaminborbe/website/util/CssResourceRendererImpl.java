package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;

@Singleton
public class CssResourceRendererImpl implements CssResourceRenderer {

	@Inject
	public CssResourceRendererImpl() {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final Collection<CssResource> cssResources) throws IOException {
		final PrintWriter out = response.getWriter();
		for (final CssResource cssResource : cssResources) {
			out.println("<link href=\"" + cssResource.getUrl() + "\" rel=\"stylesheet\" type=\"text/css\" />");
		}

	}
}
