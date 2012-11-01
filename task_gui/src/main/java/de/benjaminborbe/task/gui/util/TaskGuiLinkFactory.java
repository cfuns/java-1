package de.benjaminborbe.task.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

@Singleton
public class TaskGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public TaskGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget createTask(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CREATE_TASK, "create task");
	}

	public Widget nextTasks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CREATE_TASK, "next tasks");
	}

	public Widget completeTask(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_COMPLETE_TASK, new MapChain<String, String>().add(
				TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "complete");
	}

	public Widget uncompleteTask(final HttpServletRequest request, final Task task) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_UNCOMPLETE_TASK, new MapChain<String, String>().add(
				TaskGuiConstants.PARAMETER_TASK_ID, String.valueOf(task.getId())), "uncomplete");
	}

	public Widget completedTasks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_COMPLETE_TASK, "complete tasks");
	}

	public Widget uncompletedTasks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_UNCOMPLETE_TASK, "uncomplete tasks");
	}

}
