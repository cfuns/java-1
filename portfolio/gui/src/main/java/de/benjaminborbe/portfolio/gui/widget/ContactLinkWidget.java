package de.benjaminborbe.portfolio.gui.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactLinkWidget implements Widget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final LinkRelativWidget link = new LinkRelativWidget(request, "/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT, "Contact");
		link.render(request, response, context);
	}

}
