package de.benjaminborbe.task.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.form.FormCheckboxWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.util.SpanWidget;

@Singleton
public class TaskGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public TaskGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget completedTasks(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_COMPLETED, getLoopThrough(request), "completed");
	}

	public Widget completeTask(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_COMPLETE, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "complete").addConfirm("complete?");
	}

	public Widget completeTaskCheckbox(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {

		final MapChain<String, String[]> parameter = getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId()));
		final String target = urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_COMPLETE, parameter);
		final FormWidget formWidget = new FormWidget().addClass("taskcompleteform");
		formWidget.addFormInputWidget(new FormCheckboxWidget("done").addOnClick("if (confirm('complete?')) {window.location.href = '" + target + "';} else { this.checked = false;}"));
		return formWidget;
	}

	public Widget createSubTask(final HttpServletRequest request, final TaskIdentifier taskIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_PARENT_ID, String.valueOf(taskIdentifier)), "create subtask");
	}

	public Widget createTask(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, getLoopThrough(request), "create");
	}

	public Widget createTaskContext(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_CREATE, getLoopThrough(request), "create context");
	}

	public String createTaskUrl(final HttpServletRequest request, final TaskIdentifier taskParentIdentifier) throws UnsupportedEncodingException {
		final MapParameter parameter = getLoopThrough(request);
		if (taskParentIdentifier != null) {
			parameter.add(TaskGuiConstants.PARAMETER_TASK_PARENT_ID, String.valueOf(taskParentIdentifier));
		}
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, parameter);
	}

	public Widget deleteTask(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_DELETE, getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID,
				String.valueOf(task.getId())), "delete").addConfirm("delete?");
	}

	public Widget deleteTaskContext(final HttpServletRequest request, final TaskContext taskContext) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_DELETE, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContext.getId())), "delete").addConfirm("delete?");
	}

	private String getCurrentUri(final HttpServletRequest request) {
		return request.getRequestURI().replaceFirst(request.getContextPath(), "");
	}

	private MapParameter getLoopThrough(final HttpServletRequest request) {
		final List<String> parameters = new ArrayList<String>();
		parameters.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);

		final MapParameter result = new MapParameter();
		for (final String parameter : parameters) {
			final String[] value = request.getParameterValues(parameter);
			if (value != null && value.length > 0) {
				result.add(parameter, value);
			}
		}
		return result;
	}

	public Widget listTaskContext(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_LIST, "contexts");
	}

	public Widget nextTasks(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT, getLoopThrough(request), "next");
	}

	public Widget switchTaskContext(final HttpServletRequest request, final TaskContext taskContext) throws MalformedURLException, UnsupportedEncodingException {
		final MapParameter parameter = getLoopThrough(request);

		final Set<String> ids = new HashSet<String>();
		final String[] ls = parameter.get(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
		if (ls != null) {
			ids.addAll(Arrays.asList(ls));
		}
		final String id = String.valueOf(taskContext.getId());
		final SpanWidget content = new SpanWidget(taskContext.getName());
		if (ids.contains(id)) {
			ids.remove(id);
			content.addAttribute("class", "taskContextSelected");
		}
		else {
			ids.add(id);
			content.addAttribute("class", "taskContextNotSelected");
		}
		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, ids);
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, content);
	}

	public Widget switchTaskContextAll(final HttpServletRequest request, final List<TaskContext> taskContexts) throws MalformedURLException, UnsupportedEncodingException {
		final String[] ls = request.getParameterValues(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
		final MapParameter parameter = getLoopThrough(request);
		final Set<String> ids = new HashSet<String>();
		for (final TaskContext taskContext : taskContexts) {
			ids.add(String.valueOf(taskContext.getId()));
		}
		final SpanWidget content = new SpanWidget("all");
		if (ls != null && ls.length == taskContexts.size()) {
			content.addAttribute("class", "taskContextSelected");
		}
		else {
			content.addAttribute("class", "taskContextNotSelected");
		}

		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, ids);
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, content);
	}

	public Widget switchTaskContextNone(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {

		final String[] ls = request.getParameterValues(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);

		final SpanWidget content = new SpanWidget("none");
		if (ls != null && ls.length > 0) {
			content.addAttribute("class", "taskContextNotSelected");
		}
		else {
			content.addAttribute("class", "taskContextSelected");
		}

		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), getLoopThrough(request).add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, new String[] { null }),
				content);
	}

	public Widget taskFirstPrio(final HttpServletRequest request, final String string, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_FIRST, getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID,
				String.valueOf(task.getId())), "first");
	}

	public Widget taskLater(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_START_TOMORROW, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_START_LATER, "2h").add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "later");
	}

	public Widget taskSwapPrio(final HttpServletRequest request, final String name, final Task taskA, final Task taskB) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_SWAP_PRIO, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_ID_A, String.valueOf(taskA.getId())).add(TaskGuiConstants.PARAMETER_TASK_ID_B, String.valueOf(taskB.getId())), name);
	}

	public Widget taskTomorrow(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_START_TOMORROW, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_START_LATER, "1d").add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "tomorrow");
	}

	public Widget taskUpdate(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UPDATE, getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID,
				String.valueOf(task.getId())), "edit");
	}

	public Widget uncompletedTasks(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_UNCOMPLETED, getLoopThrough(request), "tasks");
	}

	public String uncompletedTasksUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		final MapChain<String, String[]> parameter = getLoopThrough(request);
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_UNCOMPLETED, parameter);
	}

	public Widget uncompleteTask(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UNCOMPLETE, getLoopThrough(request).add(
				TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "uncomplete").addConfirm("uncomplete?");
	}

	public Widget viewTask(final HttpServletRequest request, final String name, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_VIEW, getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID,
				String.valueOf(task.getId())), name);
	}

}
