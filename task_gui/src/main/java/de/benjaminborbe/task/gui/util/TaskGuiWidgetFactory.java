package de.benjaminborbe.task.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiWidgetFactory {

	private final AuthenticationService authenticationService;

	private final TaskService taskService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final Logger logger;

	@Inject
	public TaskGuiWidgetFactory(
			final Logger logger,
			final AuthenticationService authenticationService,
			final TaskService taskService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final TaskGuiUtil taskGuiUtil) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.taskService = taskService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiUtil = taskGuiUtil;
	}

	public Widget switchTaskContext(final HttpServletRequest request) throws AuthenticationServiceException, LoginRequiredException, TaskServiceException, MalformedURLException,
			UnsupportedEncodingException {
		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final ListWidget contextList = new ListWidget();
		final List<TaskContext> taskContexts = taskService.getTasksContexts(sessionIdentifier);
		Collections.sort(taskContexts, new TaskContextComparator());
		contextList.add("Context: ");
		contextList.add(taskGuiLinkFactory.switchTaskContextNone(request));
		contextList.add(" ");
		contextList.add(taskGuiLinkFactory.switchTaskContextAll(request));
		contextList.add(" ");
		for (final TaskContext taskContext : taskContexts) {
			contextList.add(taskGuiLinkFactory.switchTaskContext(request, taskContext));
			contextList.add(" ");
		}
		return new DivWidget(contextList);
	}

	public Widget taskListWithoutParents(final List<Task> allTasks, final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		final List<Task> tasks = taskGuiUtil.getOnlyChilds(allTasks);
		if (tasks.isEmpty()) {
			return null;
		}
		final UlWidget ul = new UlWidget();
		for (int i = 0; i < tasks.size(); ++i) {
			final Task task = tasks.get(i);
			final ListWidget widgets = new ListWidget();
			final Widget div = buildTaskListRow(request, tasks, i, task, allTasks);
			widgets.add(div);
			ul.add(widgets);
		}
		return ul;
	}

	public Widget taskListWithChilds(final List<Task> allTasks, final TaskIdentifier parentId, final HttpServletRequest request) throws MalformedURLException,
			UnsupportedEncodingException {
		final List<Task> tasks = taskGuiUtil.getChildTasks(allTasks, parentId);
		if (tasks.isEmpty()) {
			return null;
		}
		final UlWidget ul = new UlWidget();
		for (int i = 0; i < tasks.size(); ++i) {
			final Task task = tasks.get(i);
			final ListWidget widgets = new ListWidget();
			{
				final Widget div = buildTaskListRow(request, tasks, i, task, allTasks);
				widgets.add(div);
			}
			{
				widgets.add(new DivWidget(taskListWithChilds(allTasks, task.getId(), request)));
			}
			ul.add(widgets);
		}
		return ul;
	}

	private Widget buildTaskListRow(final HttpServletRequest request, final List<Task> tasks, final int position, final Task task, final List<Task> allTasks)
			throws MalformedURLException, UnsupportedEncodingException {
		final ListWidget row = new ListWidget();
		row.add(taskGuiLinkFactory.completeTask(request, task));
		row.add(" ");

		final List<String> names = new ArrayList<String>();

		Task parent = taskGuiUtil.getParent(allTasks, task);
		while (parent != null) {
			names.add(parent.getName());
			parent = taskGuiUtil.getParent(allTasks, parent);
		}
		Collections.reverse(names);
		names.add(task.getName());
		row.add(new SpanWidget(StringUtils.join(names, "/")).addAttribute("class", "taskTitle"));
		row.add(" ");

		final ListWidget options = new ListWidget();
		options.add(taskGuiLinkFactory.taskUpdate(request, task));
		options.add(" ");
		if (position > 0) {
			options.add(taskGuiLinkFactory.taskSwapPrio(request, "up", task, tasks.get(position - 1)));
			options.add(" ");
		}
		if (position < tasks.size() - 1) {
			options.add(taskGuiLinkFactory.taskSwapPrio(request, "down", task, tasks.get(position + 1)));
			options.add(" ");
		}
		options.add(taskGuiLinkFactory.createSubTask(request, task));
		options.add(" ");
		options.add(taskGuiLinkFactory.deleteTask(request, task));

		row.add(new SpanWidget(options).addAttribute("class", "taskOptions"));
		return new DivWidget(row).addAttribute("class", "taskEntry");
	}

}
