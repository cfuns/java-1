package de.benjaminborbe.html.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JavascriptResourceRenderer extends Renderer {

	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final Collection<JavascriptResource> javascriptResources)
			throws IOException;
}
