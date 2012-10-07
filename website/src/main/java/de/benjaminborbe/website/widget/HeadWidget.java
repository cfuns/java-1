package de.benjaminborbe.website.widget;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CssResourceWidget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.JavascriptResourceWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

public class HeadWidget implements Widget {

	private final Collection<JavascriptResource> javascriptResources;

	private final String title;

	private final Collection<CssResource> cssResources;

	public HeadWidget(final String title, final Collection<JavascriptResource> javascriptResources, final Collection<CssResource> cssResources) {
		this.title = title;
		this.javascriptResources = javascriptResources;
		this.cssResources = cssResources;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new TagWidget("title", title));
		widgets.add(new HtmlContentWidget("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"));
		widgets.add(new HtmlContentWidget("<meta http-equiv=\"content-language\" content=\"en\" />"));
		widgets.add(new HtmlContentWidget("<meta name=\"description\" content=\"BB\" />"));
		widgets.add(new HtmlContentWidget("<meta name=\"keywords\" content=\"BB\" />"));
		widgets.add(new HtmlContentWidget("<link rel=\"shortcut icon\" href=\"" + request.getContextPath() + "/images/favicon.ico\" />"));
		widgets.add(new JavascriptResourceWidget(javascriptResources));
		widgets.add(new CssResourceWidget(cssResources));
		final TagWidget widget = new TagWidget("head", widgets);
		widget.render(request, response, context);
	}

}
