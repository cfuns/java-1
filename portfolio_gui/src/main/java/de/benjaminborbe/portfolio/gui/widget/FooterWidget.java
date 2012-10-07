package de.benjaminborbe.portfolio.gui.widget;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.DivWidget;

public class FooterWidget extends DivWidget implements Widget {

	public FooterWidget() {
		addAttribute("class", "footer");
		addContent(new BottomNaviWidget());
	}

}
