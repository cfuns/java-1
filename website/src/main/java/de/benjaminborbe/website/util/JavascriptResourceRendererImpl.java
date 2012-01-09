package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;

@Singleton
public class JavascriptResourceRendererImpl implements JavascriptResourceRenderer {

	@Inject
	public JavascriptResourceRendererImpl() {
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final Collection<JavascriptResource> javascriptResources) throws IOException {
		final PrintWriter out = response.getWriter();
		for (final JavascriptResource javascriptResource : javascriptResources) {
			out.println("<script src=\"" + javascriptResource.getUrl() + "\"></script>");
		}
	}

}