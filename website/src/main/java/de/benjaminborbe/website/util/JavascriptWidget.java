package de.benjaminborbe.website.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;

public class JavascriptWidget implements Widget {

	private final Widget javascript;

	public JavascriptWidget(final String javascript) {
		this(new HtmlContentWidget(javascript));
	}

	public JavascriptWidget(final Widget javascript) {
		this.javascript = javascript;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final HtmlListWidget widgets = new HtmlListWidget();
		widgets.add("<script language=\"javascript\">");
		widgets.add(javascript);
		widgets.add("</script>");
		widgets.render(request, response, context);
	}
}
