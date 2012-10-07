package de.benjaminborbe.portfolio.gui.widget;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.DivWidget;

public class HeaderWidget extends DivWidget implements Widget {

	@Inject
	public HeaderWidget(final TopNaviWidget topNaviWidget) {
		addAttribute("class", "header");
		addContent(topNaviWidget);
	}

}
