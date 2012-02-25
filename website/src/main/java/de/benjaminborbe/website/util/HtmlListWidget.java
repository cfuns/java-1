package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class HtmlListWidget implements Widget {

	private final List<Widget> widgets = new ArrayList<Widget>();

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		for (final Widget widget : widgets) {
			widget.render(request, response, context);
		}
	}

	public HtmlListWidget add(final Widget widget) {
		widgets.add(widget);
		return this;
	}

	public HtmlListWidget add(final String content) {
		return add(new HtmlWidget(content));
	}

}
