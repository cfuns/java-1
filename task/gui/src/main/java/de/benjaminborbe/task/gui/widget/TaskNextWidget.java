package de.benjaminborbe.task.gui.widget;

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
import de.benjaminborbe.task.gui.util.TaskDueExpiredPredicate;
import de.benjaminborbe.task.gui.util.TaskDueTodayPredicate;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.task.gui.util.TaskStartReadyPredicate;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.Target;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.TooltipWidget;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

public class TaskNextWidget extends CompositeWidget {

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskGuiSwitchWidget taskGuiSwitchWidget;

	private final Provider<TaskCache> taskCacheProvider;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskNextWidget(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final AuthenticationService authenticationService,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final TaskGuiWidgetFactory taskGuiWidgetFactory,
		final TaskGuiUtil taskGuiUtil,
		final TaskGuiSwitchWidget taskGuiSwitchWidget,
		final Provider<TaskCache> taskCacheProvider) {
		this.calendarUtil = calendarUtil;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiSwitchWidget = taskGuiSwitchWidget;
		this.taskCacheProvider = taskCacheProvider;
		this.logger = logger;
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

		final Collection<Task> allTasks = taskGuiUtil.getTasksWithFocus(sessionIdentifier, false, taskFocus, taskContextIds);
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

	private Widget taskListWithoutParents(final SessionIdentifier sessionIdentifier, final List<Task> tasks, final TaskCache taskCache, final HttpServletRequest request,
																				final TimeZone timeZone) throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final List<Task> groupedTasks = taskGuiWidgetFactory.groupByDueState(tasks, timeZone);

		final UlWidget ul = new UlWidget();
		for (int i = 0; i < groupedTasks.size(); ++i) {
			final Task task = groupedTasks.get(i);
			final ListWidget widgets = new ListWidget();
			final Widget div = buildTaskListRow(sessionIdentifier, request, groupedTasks, i, task, taskCache, timeZone);
			widgets.add(div);
			ul.add(widgets);
		}
		ul.addClass("taskList");
		return ul;
	}

	private Widget buildTaskListRow(final SessionIdentifier sessionIdentifier, final HttpServletRequest request, final List<Task> tasks, final int position, final Task task,
																	final TaskCache taskCache, final TimeZone timeZone) throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException,
		PermissionDeniedException {

		final ListWidget row = new ListWidget();
		if (position > 0) {
			row.add(taskGuiLinkFactory.taskPrioFirst(request, taskGuiWidgetFactory.buildImage(request, "first"), task.getId()));
			row.add(" ");
			row.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "up"), task.getId(), tasks.get(position - 1).getId()));
			row.add(" ");
		} else {
			row.add(taskGuiWidgetFactory.buildImage(request, "empty"));
			row.add(" ");
			row.add(taskGuiWidgetFactory.buildImage(request, "empty"));
			row.add(" ");
		}
		if (position < tasks.size() - 1) {
			row.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "down"), task.getId(), tasks.get(position + 1).getId()));
			row.add(" ");
			row.add(taskGuiLinkFactory.taskPrioLast(request, taskGuiWidgetFactory.buildImage(request, "last"), task.getId()));
			row.add(" ");
		} else {
			row.add(taskGuiWidgetFactory.buildImage(request, "empty"));
			row.add(" ");
			row.add(taskGuiWidgetFactory.buildImage(request, "empty"));
			row.add(" ");
		}
		row.add(taskGuiLinkFactory.taskUpdate(request, taskGuiWidgetFactory.buildImage(request, "update"), task));
		row.add(" ");
//		row.add(taskGuiLinkFactory.taskView(request, taskGuiWidgetFactory.buildImage(request, "view"), task));
//		row.add(" ");
		row.add(taskGuiLinkFactory.taskCreateSubTask(request, taskGuiWidgetFactory.buildImage(request, "subtask"), task.getId()));
		row.add(" ");
//		row.add(taskGuiLinkFactory.taskDelete(request, taskGuiWidgetFactory.buildImage(request, "delete"), task));
//		row.add(" ");
		row.add(taskGuiLinkFactory.taskComplete(request, taskGuiWidgetFactory.buildImage(request, "complete"), task));
		row.add(" ");

		final ListWidget rowText = new ListWidget();

		rowText.add(taskGuiWidgetFactory.buildTaskName(sessionIdentifier, request, task, taskCache));
		rowText.add(" ");

		final String url = getUrl(sessionIdentifier, task, taskCache);

		if (url != null && url.length() > 0) {
			rowText.add(new LinkWidget(url, "goto ").addTarget(Target.BLANK));
		}

		if (task.getRepeatDue() != null && task.getRepeatDue() > 0 || task.getRepeatStart() != null && task.getRepeatStart() > 0) {
			final List<String> parts = new ArrayList<>();
			if (task.getRepeatDue() != null && task.getRepeatDue() > 0) {
				parts.add("due: " + task.getRepeatDue() + " day");
			}
			if (task.getRepeatStart() != null && task.getRepeatStart() > 0) {
				parts.add("start: " + task.getRepeatStart() + " day");
			}
			rowText.add(new TooltipWidget("(repeat) ").addTooltip(StringUtils.join(parts, " ")));
		}
		row.add(new SpanWidget(rowText).addClass("taskText"));

		final ListWidget options = new ListWidget();

		options.add(taskGuiLinkFactory.taskStartLater(request, task.getId()));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskStartTomorrow(request, task.getId()));
		options.add(" ");
		options.add(taskGuiWidgetFactory.buildImage(request, "empty"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.INBOX, "inbox"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.TODAY, "today"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.NEXT, "next"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.SOMEDAY, "someday"));

		row.add(new SpanWidget(options).addAttribute("class", "taskOptions"));
		final DivWidget div = new DivWidget(row).addClass("taskEntry");
		if (task.getDue() != null) {
			final TaskDueTodayPredicate taskDueTodayPredicate = new TaskDueTodayPredicate(logger, calendarUtil, timeZone);
			final TaskDueExpiredPredicate taskDueExpiredPredicate = new TaskDueExpiredPredicate(logger, calendarUtil, timeZone);
			if (taskDueTodayPredicate.apply(task)) {
				div.addClass("dueToday");
			} else if (taskDueExpiredPredicate.apply(task)) {
				div.addClass("dueExpired");
			}
		}
		if (task.getStart() != null) {
			final TaskStartReadyPredicate taskStartReadyPredicate = new TaskStartReadyPredicate(logger, calendarUtil, timeZone);
			if (!taskStartReadyPredicate.apply(task)) {
				div.addClass("startNotReached");
			}
		}
		return div;
	}

	private String getUrl(final SessionIdentifier sessionIdentifier, final Task task, final TaskCache taskCache) throws TaskServiceException, LoginRequiredException,
		PermissionDeniedException {
		if (task.getUrl() != null && task.getUrl().length() > 0) {
			return task.getUrl();
		}
		final Task parent = taskCache.getParent(sessionIdentifier, task);
		if (parent != null) {
			return getUrl(sessionIdentifier, parent, taskCache);
		}
		return null;
	}

}
