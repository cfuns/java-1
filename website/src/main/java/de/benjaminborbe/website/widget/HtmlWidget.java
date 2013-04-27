package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;

public class HtmlWidget implements Widget {

	private HeadWidget headWidget;

	private BodyWidget bodyWidget;

	public HtmlWidget() {
	}

	public HtmlWidget(final HeadWidget headWidget) {
		addHeadWidget(headWidget);
	}

	public HtmlWidget(final BodyWidget bodyWidget) {
		addBodyWidget(bodyWidget);
	}

	public HtmlWidget(final HeadWidget headWidget, final BodyWidget bodyWidget) {
		addHeadWidget(headWidget);
		addBodyWidget(bodyWidget);
	}

	public HtmlWidget addBodyWidget(final BodyWidget bodyWidget) {
		this.bodyWidget = bodyWidget;
		return this;
	}

	public HtmlWidget addHeadWidget(final HeadWidget headWidget) {
		this.headWidget = headWidget;
		return this;
	}

	public HtmlWidget(final ExceptionWidget exceptionWidget) {
		this(new HeadWidget("Exception", new HashSet<JavascriptResource>(), new HashSet<CssResource>()), new BodyWidget(exceptionWidget));
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");

		final ListWidget widgets = new ListWidget();
		if (headWidget != null) {
			widgets.add(headWidget);
		}
		if (bodyWidget != null) {
			widgets.add(bodyWidget);
		}
		final TagWidget html = new TagWidget("html", widgets);
		html.render(request, response, context);
	}

	public HeadWidget getHeadWidget() {
		return headWidget;
	}

	public BodyWidget getBodyWidget() {
		return bodyWidget;
	}

}
