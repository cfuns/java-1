package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;

public class FormInputSubmitWidget implements FormInputWidget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<input type=\"subit\" value=\"create\">");
	}

}
