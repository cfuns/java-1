package de.benjaminborbe.portfolio.gui.widget;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.widget.BrWidget;

public class TopWidget extends DivWidget implements Widget {

	@Inject
	public TopWidget(final TopNaviWidget topNaviWidget) {
		addAttribute("class", "header");
		final ListWidget widgets = new ListWidget();

		final ListWidget logoContent = new ListWidget();
		final DivWidget logo = new DivWidget(logoContent);
		logo.addAttribute("id", "logo");

		final ListWidget nameContent = new ListWidget();
		nameContent.add("Benjamin");
		nameContent.add(new BrWidget());
		nameContent.add("Borbe");

		final SpanWidget name = new SpanWidget(nameContent);
		name.addAttribute("class", "name");
		logoContent.add(name);

		final SpanWidget photography = new SpanWidget("photography");
		photography.addAttribute("class", "photography");
		logoContent.add(photography);

		widgets.add(topNaviWidget);
		addContent(widgets);
	}
}
