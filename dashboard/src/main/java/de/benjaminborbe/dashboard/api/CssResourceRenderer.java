package de.benjaminborbe.dashboard.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CssResourceRenderer {

	void render(final HttpServletRequest request, final HttpServletResponse response, Collection<CssResource> cssResources)
			throws IOException;

}
