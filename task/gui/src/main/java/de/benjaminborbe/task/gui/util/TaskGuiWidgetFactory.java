package de.benjaminborbe.task.gui.util;

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
import de.benjaminborbe.task.gui.widget.TaskCache;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.ImageWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class TaskGuiWidgetFactory {

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final CalendarUtil calendarUtil;

	private final Logger logger;

	private final TaskComparator taskComparator;

	@Inject
	public TaskGuiWidgetFactory(
		final Logger logger,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final TaskGuiUtil taskGuiUtil,
		final CalendarUtil calendarUtil,
		final TaskComparator taskComparator) {
		this.logger = logger;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.calendarUtil = calendarUtil;
		this.taskComparator = taskComparator;
	}

	public List<Task> groupByDueState(final List<Task> tasks, final TimeZone timeZone) {

		Collections.sort(tasks, taskComparator);

		final TaskDueTodayPredicate taskDueTodayPredicate = new TaskDueTodayPredicate(logger, calendarUtil, timeZone);
		final TaskDueExpiredPredicate taskDueExpiredPredicate = new TaskDueExpiredPredicate(logger, calendarUtil, timeZone);
		final TaskDueNotExpiredPredicate taskDueNotExpiredPredicate = new TaskDueNotExpiredPredicate(logger, calendarUtil, timeZone);

		final List<Task> result = new ArrayList<Task>();
		result.addAll(Collections2.filter(tasks, taskDueExpiredPredicate));
		result.addAll(Collections2.filter(tasks, taskDueTodayPredicate));
		result.addAll(Collections2.filter(tasks, taskDueNotExpiredPredicate));
		return result;
	}

	public Widget taskListWithChilds(final SessionIdentifier sessionIdentifier, final TaskCache taskCache, final TaskIdentifier parentId, final HttpServletRequest request,
																	 final TimeZone timeZone) throws MalformedURLException, UnsupportedEncodingException, TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final List<Task> tasks = taskCache.getChildTasks(parentId);
		if (tasks.isEmpty()) {
			return null;
		}
		final UlWidget ul = new UlWidget();
		for (int i = 0; i < tasks.size(); ++i) {
			final Task task = tasks.get(i);
			final ListWidget widgets = new ListWidget();
			{
				widgets.add(buildTaskName(sessionIdentifier, request, task, taskCache));
			}
			{
				widgets.add(new DivWidget(taskListWithChilds(sessionIdentifier, taskCache, task.getId(), request, timeZone)));
			}
			ul.add(widgets);
		}
		return ul;
	}

	public Widget buildTaskName(final SessionIdentifier sessionIdentifier, final HttpServletRequest request, final Task task, final TaskCache taskCache) throws TaskServiceException,
		LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, taskCache, task, TaskGuiConstants.PARENT_NAME_LENGTH);
		return new SpanWidget(taskGuiLinkFactory.taskView(request, new StringWidget(taskName), task)).addAttribute("class", "taskTitle");
	}

	public Widget buildImage(final HttpServletRequest request, final String name) {
		return new ImageWidget(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_IMAGES + "/" + name + "-icon.png", 20, 20).addAlt(name).addClass("icon");
	}
}
