package de.benjaminborbe.website.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	private String title;

	private final List<JavascriptResource> javascriptResources = new ArrayList<JavascriptResource>();

	private final List<CssResource> cssResources = new ArrayList<CssResource>();

	public HeadWidget() {
	}

	public HeadWidget(final String title, final Collection<JavascriptResource> javascriptResources, final Collection<CssResource> cssResources) {
		addTitle(title);
		addJavascriptResources(javascriptResources);
		addCssResources(cssResources);
	}

	public HeadWidget addTitle(final String title) {
		this.title = title;
		return this;
	}

	public HeadWidget addJavascriptResource(final JavascriptResource javascriptResource) {
		javascriptResources.add(javascriptResource);
		return this;
	}

	public HeadWidget addJavascriptResources(final Collection<JavascriptResource> javascriptResources) {
		javascriptResources.addAll(javascriptResources);
		return this;
	}

	public HeadWidget addCssResource(final CssResource cssResource) {
		cssResources.add(cssResource);
		return this;
	}

	public HeadWidget addCssResources(final Collection<CssResource> cssResources) {
		cssResources.addAll(cssResources);
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		if (title != null) {
			widgets.add(new TagWidget("title", title));
		}
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
