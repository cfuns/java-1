package de.benjaminborbe.portfolio.gui.widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.inject.Inject;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;

public class BottomWidget extends CompositeWidget implements Widget {

	private final BottomNaviWidget bottomNaviWidget;

	private final CopyrightWidget copyrightWidget;

	@Inject
	public BottomWidget(final BottomNaviWidget bottomNaviWidget, final CopyrightWidget copyrightWidget) {
		this.bottomNaviWidget = bottomNaviWidget;
		this.copyrightWidget = copyrightWidget;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) {
		final DivWidget div = new DivWidget();
		div.addAttribute("id", "footer");
		final ListWidget widgets = new ListWidget();
		widgets.add(bottomNaviWidget);
		widgets.add(copyrightWidget);
		div.addContent(widgets);
		return div;
	}

}
