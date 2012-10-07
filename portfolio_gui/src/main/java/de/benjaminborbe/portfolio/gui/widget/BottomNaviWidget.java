package de.benjaminborbe.portfolio.gui.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.UlWidget;

public class BottomNaviWidget implements Widget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final UlWidget ul = new UlWidget();
		ul.add(new LinksLinkWidget());
		ul.add(new ContactLinkWidget());
		ul.render(request, response, context);
	}

}
