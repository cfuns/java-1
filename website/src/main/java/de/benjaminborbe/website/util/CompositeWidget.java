package de.benjaminborbe.website.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public abstract class CompositeWidget implements Widget {

	protected abstract Widget createWidget(HttpServletRequest request, HttpServletResponse response, HttpContext context);

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final Widget widget = createWidget(request, response, context);
		widget.render(request, response, context);
	}
}
