package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DocTypeWidget implements Widget {

	@Override
	public void render(
		final HttpServletRequest request, final HttpServletResponse response, final HttpContext context
	) throws IOException {
		response.getWriter().println("<!DOCTYPE html>");
	}
}
