package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlListWidget implements Widget {

	private final List<Widget> widgets = new ArrayList<>();

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		for (final Widget widget : widgets) {
			widget.render(request, response, context);
		}
	}

	public HtmlListWidget add(final Widget widget) {
		if (widget != null) {
			widgets.add(widget);
		}
		return this;
	}

	public HtmlListWidget add(final String content) {
		return add(new HtmlContentWidget(content));
	}

}
