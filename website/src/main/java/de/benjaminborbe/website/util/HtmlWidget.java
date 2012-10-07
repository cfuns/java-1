package de.benjaminborbe.website.util;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.widget.HeadWidget;

public class HtmlWidget implements Widget {

	private final HeadWidget headWidget;

	private final BodyWidget bodyWidget;

	public HtmlWidget(final HeadWidget headWidget, final BodyWidget bodyWidget) {
		this.headWidget = headWidget;
		this.bodyWidget = bodyWidget;
	}

	public HtmlWidget(final ExceptionWidget exceptionWidget) {
		this(new HeadWidget("Exception", new HashSet<JavascriptResource>(), new HashSet<CssResource>()), new BodyWidget());
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		final ListWidget widgets = new ListWidget();
		widgets.add(headWidget);
		widgets.add(bodyWidget);
		final TagWidget html = new TagWidget("html", widgets);
		html.render(request, response, context);
	}

}
