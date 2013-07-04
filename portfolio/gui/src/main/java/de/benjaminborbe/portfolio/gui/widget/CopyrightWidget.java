package de.benjaminborbe.portfolio.gui.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiLinkFactory;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CopyrightWidget implements Widget {

	private final PortfolioGuiLinkFactory portfolioLinkFactory;

	@Inject
	public CopyrightWidget(final PortfolioGuiLinkFactory portfolioLinkFactory) {
		this.portfolioLinkFactory = portfolioLinkFactory;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final DivWidget div = new DivWidget();
		div.addAttribute("id", "copyright");
		final ListWidget widgets = new ListWidget();
		widgets.add(portfolioLinkFactory.createCopyright(request));
		div.addContent(widgets);
		div.render(request, response, context);
	}
}
