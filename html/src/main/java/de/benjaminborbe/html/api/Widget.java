package de.benjaminborbe.html.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface Widget {

	void render(final HttpServletRequest request, final HttpServletResponse response, HttpContext context) throws PermissionDeniedException, IOException;

}
