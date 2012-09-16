package de.benjaminborbe.website.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class HtmlWidget implements Widget {

	private final Widget widget;

	public HtmlWidget(final Widget widget) {
		this.widget = widget;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new TagWidget("head"));
		widgets.add(new TagWidget("body", widget));
		final TagWidget html = new TagWidget("html", widgets);
		html.render(request, response, context);
	}

}
