package de.benjaminborbe.portfolio.gui.widget;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.DivWidget;

public class BottomWidget extends DivWidget implements Widget {

	public BottomWidget() {
		addAttribute("class", "footer");
		addContent(new BottomNaviWidget());
	}

}
