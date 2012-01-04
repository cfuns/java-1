package de.benjaminborbe.html.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HTML {

	void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}
