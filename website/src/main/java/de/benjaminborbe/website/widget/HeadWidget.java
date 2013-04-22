package de.benjaminborbe.website.widget;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CssResourceWidget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.JavascriptResourceWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HeadWidget implements Widget {

	private String title;

	private final List<JavascriptResource> javascriptResources = new ArrayList<>();

	private final List<CssResource> cssResources = new ArrayList<>();

	private final ListWidget widgets = new ListWidget();

	public HeadWidget() {
	}

	public HeadWidget(final String title, final Collection<JavascriptResource> javascriptResources, final Collection<CssResource> cssResources) {
		addTitle(title);
		addCssResources(cssResources);
		addJavascriptResources(javascriptResources);
	}

	public HeadWidget addTitle(final String title) {
		this.title = title;
		return this;
	}

	public HeadWidget addJavascriptResource(final JavascriptResource javascriptResource) {
		this.javascriptResources.add(javascriptResource);
		return this;
	}

	public HeadWidget addJavascriptResources(final Collection<JavascriptResource> javascriptResources) {
		this.javascriptResources.addAll(javascriptResources);
		return this;
	}

	public HeadWidget addCssResource(final CssResource cssResource) {
		this.cssResources.add(cssResource);
		return this;
	}

	public HeadWidget addCssResources(final Collection<CssResource> cssResources) {
		this.cssResources.addAll(cssResources);
		return this;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget list = new ListWidget();
		if (title != null) {
			list.add(new TagWidget("title", title));
		}
		list.add(new HtmlContentWidget("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" />"));
		list.add(new HtmlContentWidget("<meta http-equiv=\"content-language\" content=\"en\" />"));
		list.add(new HtmlContentWidget("<meta name=\"description\" content=\"BB\" />"));
		list.add(new HtmlContentWidget("<meta name=\"keywords\" content=\"BB\" />"));
		list.add(new HtmlContentWidget("<link rel=\"search\" type=\"application/opensearchdescription+xml\" title=\"BB\" href=\"" + request.getContextPath() + "/search/osd.xml\" />"));
		list.add(new HtmlContentWidget("<link rel=\"shortcut icon\" href=\"" + request.getContextPath() + "/images/favicon.ico\" />"));
		list.add(new CssResourceWidget(cssResources));
		list.add(new JavascriptResourceWidget(javascriptResources));
		list.add(widgets);
		final TagWidget widget = new TagWidget("head", list);
		widget.render(request, response, context);
	}

	public HeadWidget add(final Widget widget) {
		widgets.add(widget);
		return this;
	}
}
