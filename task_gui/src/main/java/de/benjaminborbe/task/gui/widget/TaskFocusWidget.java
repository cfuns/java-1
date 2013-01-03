package de.benjaminborbe.task.gui.widget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

public class TaskFocusWidget extends CompositeWidget {

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();

		final UlWidget ul = new UlWidget();
		ul.add("Inbox");
		ul.add("Today");
		ul.add("Next");
		ul.add("Someday");

		widgets.add(ul);
		return new DivWidget(widgets).addClass("taskFocus");
	}

}
