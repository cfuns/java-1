package de.benjaminborbe.task.gui.widget;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

public class TaskNextWidget extends CompositeWidget {

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskGuiSwitchWidget taskGuiSwitchWidget;

	private final Provider<TaskCache> taskCacheProvider;

	@Inject
	public TaskNextWidget(
			final AuthenticationService authenticationService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final TaskGuiWidgetFactory taskGuiWidgetFactory,
			final TaskGuiUtil taskGuiUtil,
			final TaskGuiSwitchWidget taskGuiSwitchWidget,
			final Provider<TaskCache> taskCacheProvider) {
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiSwitchWidget = taskGuiSwitchWidget;
		this.taskCacheProvider = taskCacheProvider;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget("Task - Next"));

		widgets.add(taskGuiSwitchWidget);

		final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
		final List<String> taskContextIds = taskGuiUtil.getSelectedTaskContextIds(request);
		final TaskFocus taskFocus = taskGuiUtil.getSelectedTaskFocus(request);
		final TimeZone timeZone = authenticationService.getTimeZone(sessionIdentifier);

		final TaskCache taskCache = taskCacheProvider.get();
		final Collection<Task> allTasks = taskGuiUtil.getTasksNotCompleted(sessionIdentifier, taskFocus, taskContextIds);
		taskCache.addAll(allTasks);

		final List<Task> childTasks = taskGuiUtil.getOnlyChilds(allTasks);
		final List<Task> tasks = taskGuiUtil.filterNotStarted(childTasks, timeZone);
		widgets.add(taskListWithoutParents(sessionIdentifier, tasks, taskCache, request, timeZone));

		final ListWidget links = new ListWidget();
		links.add(taskGuiLinkFactory.tasksUncompleted(request));
		links.add(" ");
		links.add(taskGuiLinkFactory.taskCreate(request));
		links.add(" ");
		links.add(taskGuiLinkFactory.tasksCompleted(request));
		links.add(" ");
		links.add(taskGuiLinkFactory.taskContextList(request));
		widgets.add(links);
		return new DivWidget(widgets).addClass("taskNext");
	}

	public Widget taskListWithoutParents(final SessionIdentifier sessionIdentifier, final List<Task> tasks, final TaskCache taskCache, final HttpServletRequest request,
			final TimeZone timeZone) throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final List<Task> groupedTasks = taskGuiWidgetFactory.groupByDueState(tasks, timeZone);

		final UlWidget ul = new UlWidget();
		for (int i = 0; i < groupedTasks.size(); ++i) {
			final Task task = groupedTasks.get(i);
			final ListWidget widgets = new ListWidget();
			final Widget div = taskGuiWidgetFactory.buildTaskListRow(sessionIdentifier, request, groupedTasks, i, task, taskCache, timeZone);
			widgets.add(div);
			ul.add(widgets);
		}
		ul.addClass("taskList");
		return ul;
	}

}
