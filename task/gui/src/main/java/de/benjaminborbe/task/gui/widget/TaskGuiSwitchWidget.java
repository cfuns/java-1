package de.benjaminborbe.task.gui.widget;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.gui.util.TaskContextComparator;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskGuiSwitchWidget extends CompositeWidget {

	private final AuthenticationService authenticationService;

	private final TaskService taskService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	@Inject
	public TaskGuiSwitchWidget(final TaskGuiLinkFactory taskGuiLinkFactory, final TaskService taskService, final AuthenticationService authenticationService) {
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final ListWidget contextList = new ListWidget();
		final List<TaskContext> taskContexts = new ArrayList<>(taskService.getTaskContexts(sessionIdentifier));
		Collections.sort(taskContexts, new TaskContextComparator());
		contextList.add("Context: ");
		contextList.add(taskGuiLinkFactory.taskContextSwitchNone(request));
		contextList.add(" ");
		contextList.add(taskGuiLinkFactory.taskContextSwitchAll(request));
		contextList.add(" ");
		for (final TaskContext taskContext : taskContexts) {
			contextList.add(taskGuiLinkFactory.taskContextSwitch(request, taskContext));
			contextList.add(" ");
		}
		return new DivWidget(contextList);
	}

}
