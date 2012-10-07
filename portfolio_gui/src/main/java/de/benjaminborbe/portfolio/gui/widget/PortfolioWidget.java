package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.BodyWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.HtmlWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.HeadWidget;
import de.benjaminborbe.website.widget.VoidWidget;

public class PortfolioWidget implements Widget {

	private String title;

	private final List<JavascriptResource> javascriptResources = new ArrayList<JavascriptResource>();

	private final List<CssResource> cssResources = new ArrayList<CssResource>();

	private final HeaderWidget headerWidget;

	private final FooterWidget footerWidget;

	private Widget content;

	@Inject
	public PortfolioWidget(final HeaderWidget headerWidget, final FooterWidget footerWidget) {
		this.headerWidget = headerWidget;
		this.footerWidget = footerWidget;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final HeadWidget headWidget = new HeadWidget(title, javascriptResources, cssResources);

		final ListWidget widgets = new ListWidget();
		widgets.add(headerWidget);
		widgets.add(new DivWidget(getContent()).addAttribute("class", "content"));
		widgets.add(footerWidget);

		final BodyWidget bodyWidget = new BodyWidget(widgets);
		final HtmlWidget widget = new HtmlWidget(headWidget, bodyWidget);
		widget.render(request, response, context);
	}

	private Widget getContent() {
		return content != null ? content : new VoidWidget();
	}

	public PortfolioWidget addContent(final Widget content) {
		this.content = content;
		return this;
	}
}
