package de.benjaminborbe.website.util;

import de.benjaminborbe.html.api.Widget;

public class UlWidget extends TagWidget {

	private static final String TAG = "ul";

	private final ListWidget list = new ListWidget();

	public UlWidget() {
		super(TAG);
		addContent(list);
	}

	public UlWidget add(final LiWidget widget) {
		list.add(widget);
		return this;
	}

	public UlWidget add(final String content) {
		return add(new LiWidget(content));
	}

	public UlWidget add(final Widget widget) {
		return add(new LiWidget(widget));
	}

	// @Override
	// public void render(final HttpServletRequest request, final HttpServletResponse
	// response, final HttpContext context) throws IOException, PermissionDeniedException {
	// list.render(request, response, context);
	// }

}
