package de.benjaminborbe.task.gui.widget;

import com.google.inject.Inject;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TaskFocusWidget extends CompositeWidget {

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	@Inject
	public TaskFocusWidget(final TaskGuiLinkFactory taskGuiLinkFactory) {
		this.taskGuiLinkFactory = taskGuiLinkFactory;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		final UlWidget ul = new UlWidget();
		ul.add(taskGuiLinkFactory.taskFocusSwitch(request, "Inbox", TaskFocus.INBOX));
		ul.add(taskGuiLinkFactory.taskFocusSwitch(request, "Today", TaskFocus.TODAY));
		ul.add(taskGuiLinkFactory.taskFocusSwitch(request, "Next", TaskFocus.NEXT));
		ul.add(taskGuiLinkFactory.taskFocusSwitch(request, "Someday", TaskFocus.SOMEDAY));
		widgets.add(ul);
		return new DivWidget(widgets).addClass("taskFocus");
	}

}
