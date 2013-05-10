package de.benjaminborbe.task.gui.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

import javax.inject.Inject;
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
		for (final TaskFocus taskFocus : TaskFocus.values()) {
			ul.add(taskGuiLinkFactory.taskFocusSwitch(request, buildName(taskFocus), taskFocus));
		}
		widgets.add(ul);
		return new DivWidget(widgets).addClass("taskFocus");
	}

	private String buildName(final TaskFocus taskFocus) {
		final String name = taskFocus.name();
		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

}
