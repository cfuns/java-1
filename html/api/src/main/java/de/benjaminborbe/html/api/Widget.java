package de.benjaminborbe.html.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Widget {

	void render(final HttpServletRequest request, final HttpServletResponse response, HttpContext context) throws IOException;

}
