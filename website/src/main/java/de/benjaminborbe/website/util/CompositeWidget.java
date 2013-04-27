package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class CompositeWidget implements Widget {

	protected abstract Widget createWidget(HttpServletRequest request, HttpServletResponse response, HttpContext context) throws Exception;

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final Widget widget = createWidget(request, response, context);
			widget.render(request, response, context);
		} catch (final Exception e) {
			final Widget widget = createExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	protected Widget createExceptionWidget(final Exception e) {
		return new ExceptionWidget(e);
	}
}
