package de.benjaminborbe.portfolio.gui.widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BodyWidget;
import de.benjaminborbe.website.widget.GoogleAnalyticsScriptWidget;
import de.benjaminborbe.website.widget.HeadWidget;
import de.benjaminborbe.website.widget.HtmlWidget;
import de.benjaminborbe.website.widget.VoidWidget;

public class PortfolioLayoutWidget extends CompositeWidget implements Widget {

	private String title;

	private final TopWidget topWidget;

	private final BottomWidget footerWidget;

	private Widget content;

	@Inject
	public PortfolioLayoutWidget(final TopWidget topWidget, final BottomWidget footerWidget) {
		this.topWidget = topWidget;
		this.footerWidget = footerWidget;
	}

	private CssResource buildCssResource(final HttpServletRequest request, final String path) {
		return new CssResourceImpl(request.getContextPath() + "/" + PortfolioGuiConstants.NAME + "/css/" + path);
	}

	private JavascriptResource buildJavascriptResource(final HttpServletRequest request, final String path) {
		return new JavascriptResourceImpl(request.getContextPath() + "/" + PortfolioGuiConstants.NAME + "/js/" + path);
	}

	private Widget getContent() {
		return content != null ? content : new VoidWidget();
	}

	public PortfolioLayoutWidget addContent(final Widget content) {
		this.content = content;
		return this;
	}

	public PortfolioLayoutWidget addTitle(final String title) {
		this.title = title;
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final HeadWidget headWidget = new HeadWidget();
		headWidget.addTitle(title);

		headWidget.addJavascriptResource(buildJavascriptResource(request, "prototype.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "scriptaculous.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "builder.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "effects.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "lightbox.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "script.js"));

		headWidget.addCssResource(buildCssResource(request, "style.css"));
		headWidget.addCssResource(buildCssResource(request, "lightbox.css"));

		headWidget.add(new GoogleAnalyticsScriptWidget());

		final ListWidget widgets = new ListWidget();
		widgets.add(topWidget);
		widgets.add(new DivWidget(getContent()).addAttribute("id", "content"));
		widgets.add(footerWidget);

		final BodyWidget bodyWidget = new BodyWidget(widgets);
		return new HtmlWidget(headWidget, bodyWidget);
	}
}
