package de.benjaminborbe.task.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiWidgetFactory {

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskDueTodayPredicate taskDueTodayPredicate;

	private final TaskDueExpiredPredicate taskDueExpiredPredicate;

	private final TaskDueNotExpiredPredicate taskDueNotExpiredPredicate;

	private final TaskStartReadyPredicate taskStartReadyPredicate;

	@Inject
	public TaskGuiWidgetFactory(
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final TaskGuiUtil taskGuiUtil,
			final TaskDueTodayPredicate taskDueTodayPredicate,
			final TaskDueExpiredPredicate taskDueExpiredPredicate,
			final TaskDueNotExpiredPredicate taskDueNotExpiredPredicate,
			final TaskStartReadyPredicate taskStartReadyPredicate) {
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.taskDueTodayPredicate = taskDueTodayPredicate;
		this.taskDueExpiredPredicate = taskDueExpiredPredicate;
		this.taskDueNotExpiredPredicate = taskDueNotExpiredPredicate;
		this.taskStartReadyPredicate = taskStartReadyPredicate;
	}

	public Widget taskListWithoutParents(final SessionIdentifier sessionIdentifier, final List<Task> tasks, final List<Task> allTasks, final HttpServletRequest request)
			throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final List<Task> groupedTasks = groupByDueState(tasks);

		final UlWidget ul = new UlWidget();
		for (int i = 0; i < groupedTasks.size(); ++i) {
			final Task task = groupedTasks.get(i);
			final ListWidget widgets = new ListWidget();
			final Widget div = buildTaskListRow(sessionIdentifier, request, groupedTasks, i, task, allTasks);
			widgets.add(div);
			ul.add(widgets);
		}
		return ul;
	}

	private List<Task> groupByDueState(final List<Task> tasks) {
		final List<Task> result = new ArrayList<Task>();
		result.addAll(Collections2.filter(tasks, taskDueExpiredPredicate));
		result.addAll(Collections2.filter(tasks, taskDueTodayPredicate));
		result.addAll(Collections2.filter(tasks, taskDueNotExpiredPredicate));
		return result;
	}

	public Widget taskListWithChilds(final SessionIdentifier sessionIdentifier, final List<Task> allTasks, final TaskIdentifier parentId, final HttpServletRequest request)
			throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final List<Task> tasks = taskGuiUtil.getChildTasks(allTasks, parentId);
		if (tasks.isEmpty()) {
			return null;
		}
		final UlWidget ul = new UlWidget();
		for (int i = 0; i < tasks.size(); ++i) {
			final Task task = tasks.get(i);
			final ListWidget widgets = new ListWidget();
			{
				final Widget div = buildTaskListRow(sessionIdentifier, request, tasks, i, task, allTasks);
				widgets.add(div);
			}
			{
				widgets.add(new DivWidget(taskListWithChilds(sessionIdentifier, allTasks, task.getId(), request)));
			}
			ul.add(widgets);
		}
		return ul;
	}

	private Widget buildTaskListRow(final SessionIdentifier sessionIdentifier, final HttpServletRequest request, final List<Task> tasks, final int position, final Task task,
			final List<Task> allTasks) throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {

		final ListWidget row = new ListWidget();
		row.add(taskGuiLinkFactory.completeTaskCheckbox(request, task));
		row.add(" ");
		final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, allTasks, task, TaskGuiConstants.PARENT_NAME_LENGTH);
		row.add(new SpanWidget(taskGuiLinkFactory.viewTask(request, taskName, task)).addAttribute("class", "taskTitle"));
		row.add(" ");

		if (task.getUrl() != null && task.getUrl().length() > 0) {
			row.add(new LinkWidget(task.getUrl(), "goto ").addTarget(Target.BLANK));
		}

		if (task.getRepeatDue() != null && task.getRepeatDue() > 0 || task.getRepeatStart() != null && task.getRepeatStart() > 0) {
			row.add("(repeat) ");
		}

		final ListWidget options = new ListWidget();
		options.add(taskGuiLinkFactory.taskUpdate(request, task));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskLater(request, task));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskTomorrow(request, task));
		options.add(" ");
		if (position > 0) {
			options.add(taskGuiLinkFactory.taskFirstPrio(request, "first", task));
			options.add(" ");

			options.add(taskGuiLinkFactory.taskSwapPrio(request, "up", task, tasks.get(position - 1)));
			options.add(" ");
		}
		if (position < tasks.size() - 1) {
			options.add(taskGuiLinkFactory.taskSwapPrio(request, "down", task, tasks.get(position + 1)));
			options.add(" ");
		}
		options.add(taskGuiLinkFactory.createSubTask(request, task.getId()));
		options.add(" ");
		options.add(taskGuiLinkFactory.deleteTask(request, task));

		row.add(new SpanWidget(options).addAttribute("class", "taskOptions"));
		final DivWidget div = new DivWidget(row).addClass("taskEntry");
		if (task.getDue() != null) {
			if (taskDueTodayPredicate.apply(task)) {
				div.addClass("dueToday");
			}
			else if (taskDueExpiredPredicate.apply(task)) {
				div.addClass("dueExpired");
			}
		}
		if (task.getStart() != null) {
			if (!taskStartReadyPredicate.apply(task)) {
				div.addClass("startNotReached");
			}
		}
		return div;
	}
}
