package de.benjaminborbe.html.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JavascriptResourceRendererImpl implements JavascriptResourceRenderer {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final Collection<JavascriptResource> javascriptResources) throws IOException {
		final PrintWriter out = response.getWriter();
		for (final JavascriptResource javascriptResource : javascriptResources) {
			out.println("<script src=\"" + javascriptResource.getUrl() + "\"></script>");
		}
	}

}
