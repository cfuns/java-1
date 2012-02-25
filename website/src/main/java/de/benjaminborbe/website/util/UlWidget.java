package de.benjaminborbe.website.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class UlWidget implements Widget {

	private static final String TAG = "ul";

	private final ListWidget list = new ListWidget();

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

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, PermissionDeniedException {
		final PrintWriter out = response.getWriter();
		out.print("<");
		out.print(TAG);
		out.print(">");
		list.render(request, response, context);
		out.print("</");
		out.print(TAG);
		out.print(">");
	}

}
