package de.benjaminborbe.html.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CssResourceRenderer extends Renderer {

	void render(HttpServletRequest request, HttpServletResponse response, Collection<CssResource> cssResources) throws IOException;

}
