package de.benjaminborbe.task.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class TaskGuiWidgetFactory {

	private final AuthenticationService authenticationService;

	private final TaskService taskService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	@Inject
	public TaskGuiWidgetFactory(final AuthenticationService authenticationService, final TaskService taskService, final TaskGuiLinkFactory taskGuiLinkFactory) {
		this.authenticationService = authenticationService;
		this.taskService = taskService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
	}

	public Widget switchTaskContext(final HttpServletRequest request) throws AuthenticationServiceException, LoginRequiredException, TaskServiceException, MalformedURLException,
			UnsupportedEncodingException {
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final ListWidget contextList = new ListWidget();
		final List<TaskContext> taskContexts = taskService.getTasksContexts(sessionIdentifier);
		Collections.sort(taskContexts, new TaskContextComparator());
		contextList.add("Context: ");
		contextList.add(taskGuiLinkFactory.switchTaskContext(request));
		contextList.add(" ");
		for (final TaskContext taskContext : taskContexts) {
			contextList.add(taskGuiLinkFactory.switchTaskContext(request, taskContext));
			contextList.add(" ");
		}
		return contextList;
	}

}
