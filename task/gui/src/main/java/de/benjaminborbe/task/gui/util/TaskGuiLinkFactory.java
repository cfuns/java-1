package de.benjaminborbe.task.gui.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskAttachment;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.widget.TooltipWidget;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class TaskGuiLinkFactory {

	private final UrlUtil urlUtil;

	private final TaskGuiUtil taskGuiUtil;

	@Inject
	public TaskGuiLinkFactory(final UrlUtil urlUtil, final TaskGuiUtil taskGuiUtil) {
		this.urlUtil = urlUtil;
		this.taskGuiUtil = taskGuiUtil;
	}

	private Widget buildStartLater(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier,
		final String time,
		final String name
	) throws MalformedURLException,
		UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_START_TOMORROW, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_START_LATER, time).add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(taskIdentifier)), name);
		return new TooltipWidget(link).addTooltip("Set task start to " + name);
	}

	private String getCurrentUri(final HttpServletRequest request) {
		return request.getRequestURI().replaceFirst(request.getContextPath(), "");
	}

	private MapParameter getLoopThrough(final HttpServletRequest request) {
		final List<String> parameters = new ArrayList<String>();
		parameters.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
		parameters.add(TaskGuiConstants.PARAMETER_SELECTED_TASKFOCUS);

		final MapParameter result = new MapParameter();
		for (final String parameter : parameters) {
			final String[] value = request.getParameterValues(parameter);
			if (value != null && value.length > 0) {
				result.add(parameter, value);
			}
		}
		return result;
	}

	public Widget taskComplete(final HttpServletRequest request, final Widget name, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		final LinkWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_COMPLETE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), name).addConfirm("Complete task: \"" + task.getName() + "\"?");
		return new TooltipWidget(link).addTooltip("Complete task");
	}

	public Widget taskContextCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_CREATE, getLoopThrough(request), "create context");
	}

	public Widget taskContextDelete(final HttpServletRequest request, final TaskContext taskContext) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_DELETE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContext.getId())), "delete").addConfirm("Delete taskcontent: \"" + taskContext.getName() + "\"?");
	}

	public Widget taskContextList(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_LIST, "contexts");
	}

	public String taskContextListUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		final MapChain<String, String[]> parameter = getLoopThrough(request);
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_LIST, parameter);
	}

	public Widget taskFocusSwitch(
		final HttpServletRequest request,
		final String name,
		final TaskFocus taskFocus
	) throws MalformedURLException, UnsupportedEncodingException {
		final MapParameter parameter = getLoopThrough(request);
		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKFOCUS, taskFocus.name());

		final TaskFocus selectedTaskFocus = taskGuiUtil.getSelectedTaskFocus(request);
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, new SpanWidget(name).addClass(taskFocus.equals(selectedTaskFocus) ? "taskFocusSelected"
			: "taskFocusNotSelected"));
	}

	public Widget taskContextSwitch(final HttpServletRequest request, final TaskContext taskContext) throws MalformedURLException, UnsupportedEncodingException {
		final MapParameter parameter = getLoopThrough(request);

		final Set<String> ids = new HashSet<String>();
		final List<String> ls = taskGuiUtil.getSelectedTaskContextIds(request);
		if (ls != null) {
			ids.addAll(ls);
		}
		final String id = String.valueOf(taskContext.getId());
		final SpanWidget content = new SpanWidget(taskContext.getName());
		if (ids.contains(id)) {
			ids.remove(id);
			content.addAttribute("class", "taskContextSelected");
		} else {
			ids.add(id);
			content.addAttribute("class", "taskContextNotSelected");
		}
		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, ids);
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, content);
	}

	public Widget taskContextSwitchAll(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		final List<String> taskContextIds = taskGuiUtil.getSelectedTaskContextIds(request);
		final MapParameter parameter = getLoopThrough(request);
		final SpanWidget content = new SpanWidget(TaskGuiConstants.ALL);
		if (taskContextIds != null && taskContextIds.size() == 1 && TaskGuiConstants.ALL.equals(taskContextIds.get(0))) {
			content.addAttribute("class", "taskContextSelected");
		} else {
			content.addAttribute("class", "taskContextNotSelected");
		}
		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, Arrays.asList(TaskGuiConstants.ALL));
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, content);
	}

	public Widget taskContextSwitchNone(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		final List<String> taskContextIds = taskGuiUtil.getSelectedTaskContextIds(request);
		final MapParameter parameter = getLoopThrough(request);
		final SpanWidget content = new SpanWidget(TaskGuiConstants.NONE);
		if (taskContextIds != null && taskContextIds.size() == 1 && TaskGuiConstants.NONE.equals(taskContextIds.get(0))) {
			content.addAttribute("class", "taskContextSelected");
		} else {
			content.addAttribute("class", "taskContextNotSelected");
		}
		parameter.add(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID, Arrays.asList(TaskGuiConstants.NONE));
		return new LinkRelativWidget(urlUtil, request, getCurrentUri(request), parameter, content);
	}

	public Widget taskContextUpdate(final HttpServletRequest request, final TaskContext taskContext) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_UPDATE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContext.getId())), "edit");
	}

	public Widget taskCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, getLoopThrough(request), "create");
	}

	public Widget taskCreateSubTask(final HttpServletRequest request, final Widget name, final TaskIdentifier taskIdentifier) throws MalformedURLException,
		UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_PARENT_ID, String.valueOf(taskIdentifier)), name);
		return new TooltipWidget(link).addTooltip("Create subtask");
	}

	public String taskCreateUrl(final HttpServletRequest request, final TaskIdentifier taskParentIdentifier) throws UnsupportedEncodingException {
		final MapParameter parameter = getLoopThrough(request);
		if (taskParentIdentifier != null) {
			parameter.add(TaskGuiConstants.PARAMETER_TASK_PARENT_ID, String.valueOf(taskParentIdentifier));
		}
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE, parameter);
	}

	public Widget taskDelete(final HttpServletRequest request, final Widget name, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		final LinkWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_DELETE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), name).addConfirm("Delete task: \"" + task.getName() + "\"?");
		return new TooltipWidget(link).addTooltip("Delete task");
	}

	public Widget taskPrioFirst(
		final HttpServletRequest request,
		final Widget name,
		final TaskIdentifier taskIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_FIRST, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(taskIdentifier)), name);
		return new TooltipWidget(link).addTooltip("Change to highest priority");
	}

	public Widget taskPrioLast(
		final HttpServletRequest request,
		final Widget name,
		final TaskIdentifier taskIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_LAST, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(taskIdentifier)), name);
		return new TooltipWidget(link).addTooltip("Change to lowest priority");
	}

	public Widget taskPrioSwap(final HttpServletRequest request, final Widget name, final TaskIdentifier taskIdentifierA, final TaskIdentifier taskIdentifierB)
		throws MalformedURLException, UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_SWAP_PRIO, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID_A, String.valueOf(taskIdentifierA)).add(TaskGuiConstants.PARAMETER_TASK_ID_B, String.valueOf(taskIdentifierB)), name);
		return new TooltipWidget(link).addTooltip("Change priority");
	}

	public Widget tasksCompleted(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_COMPLETED, getLoopThrough(request), "completed");
	}

	public Widget tasksNext(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT, getLoopThrough(request), "next");
	}

	public String tasksNextUrl(final HttpServletRequest request) throws UnsupportedEncodingException {
		final MapChain<String, String[]> parameter = getLoopThrough(request);
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT, parameter);
	}

	public Widget taskStartLater(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return buildStartLater(request, taskIdentifier, "2h", "+2h");
	}

	public Widget taskStartTomorrow(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return buildStartLater(request, taskIdentifier, "1d", "+1d");
	}

	public Widget tasksUncompleted(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_UNCOMPLETED, getLoopThrough(request), "tasks");
	}

	public Widget taskUncomplete(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UNCOMPLETE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "uncomplete").addConfirm("Uncomplete task: \"" + task.getName() + "\"?");
	}

	public Widget taskUpdate(final HttpServletRequest request, final Widget name, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UPDATE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), name);
		return new TooltipWidget(link).addTooltip("Modify task");
	}

	public Widget taskView(final HttpServletRequest request, final Widget name, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		final LinkRelativWidget link = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_VIEW, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), name);
		return new TooltipWidget(link).addTooltip("View task");
	}

	public String taskViewUrl(final HttpServletRequest request, final TaskIdentifier taskIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_VIEW,
			getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(taskIdentifier)));
	}

	public Widget taskContextUser(
		final HttpServletRequest request,
		final TaskContextIdentifier taskContextIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_USER, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContextIdentifier)), "user");
	}

	public Widget taskContextUserRemove(final HttpServletRequest request, final TaskContextIdentifier taskContextIdentifier, final UserIdentifier userIdentifier)
		throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_USER_REMOVE, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContextIdentifier)).add(TaskGuiConstants.PARAMETER_USER_ID, String.valueOf(userIdentifier)), "remove");
	}

	public String taskContextUserUrl(final HttpServletRequest request, final TaskContextIdentifier taskContextIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_USER,
			getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, String.valueOf(taskContextIdentifier)));
	}

	public Widget taskUpdateFocus(final HttpServletRequest request, final Task task, final TaskFocus taskFocus, final String name) throws MalformedURLException,
		UnsupportedEncodingException {
		return new TooltipWidget(

			new SpanWidget(

				new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UPDATE_FOCUS, getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID,
					String.valueOf(task.getId())).add(TaskGuiConstants.PARAMETER_TASK_FOCUS, taskFocus.name()), name)

			).addClass(taskFocus.equals(task.getFocus()) ? "taskFocusSelected" : "taskFocusNotSelected")

		).addTooltip("update focus to " + name);
	}

	public Widget taskSelectTaskContext(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier,
		final TaskContextIdentifier taskContextIdentifier,
		final Widget name
	)
		throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_SELECT_TASKCONTEXT, getLoopThrough(request).add(
			TaskGuiConstants.PARAMETER_TASKCONTEXT_ID, taskContextIdentifier).add(TaskGuiConstants.PARAMETER_TASK_ID, taskIdentifier), name);
	}

	public Widget taskAttachmentCreate(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier
	) throws UnsupportedEncodingException, MalformedURLException {
		return taskAttachmentCreate(request, taskIdentifier, new StringWidget("add attachment"));
	}

	public Widget taskAttachmentCreate(
		final HttpServletRequest request,
		final TaskIdentifier taskIdentifier,
		final Widget widget
	) throws UnsupportedEncodingException, MalformedURLException {
		final LinkRelativWidget linkRelativWidget = new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKATTACHMENT_CREATE,
			getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(taskIdentifier)), widget);
		return new TooltipWidget(linkRelativWidget).addTooltip("Upload attachment");
	}

	public Widget taskAttachmentDelete(
		final HttpServletRequest request,
		final TaskAttachment taskAttachment
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKATTACHMENT_DELETE,
			getLoopThrough(request).add(TaskGuiConstants.PARAMETER_TASKATTACHMENT_ID, String.valueOf(taskAttachment.getId())), "delete").addConfirm("delete attachment " + taskAttachment.getName() + "?");
	}

	public Widget taskAttachmentDownload(final HttpServletRequest request, final TaskAttachment attachment) throws MalformedURLException {
		return new LinkRelativWidget(request, "/filestorage/get/" + attachment.getFile().getId(), attachment.getName());
	}
}
