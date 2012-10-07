package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class LinksLinkWidget implements Widget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final LinkRelativWidget link = new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS, "Links");
		link.render(request, response, context);
	}

}
