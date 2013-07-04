package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BodyWidget implements Widget {

	private static final String TAG = "body";

	private final ListWidget widgets = new ListWidget();

	public BodyWidget() {
	}

	public BodyWidget(final Widget widget) {
		add(widget);
	}

	public BodyWidget add(final Widget widget) {
		widgets.add(widget);
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final TagWidget body = new TagWidget(TAG, widgets);
		body.render(request, response, context);
	}

}
