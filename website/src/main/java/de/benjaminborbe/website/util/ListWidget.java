package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class ListWidget implements Widget {

	private final List<Widget> widgets = new ArrayList<Widget>();

	public ListWidget addWidget(final Widget widget) {
		widgets.add(widget);
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		for (final Widget widget : widgets) {
			widget.render(request, response, context);
		}
	}

}
