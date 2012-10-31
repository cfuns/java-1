package de.benjaminborbe.task.gui.util;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.website.link.LinkRelativWidget;

@Singleton
public class TaskGuiLinkFactory {

	@Inject
	public TaskGuiLinkFactory() {
	}

	public Widget createTask(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CREATE, "create task");
	}

	public Widget nextTasks(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CREATE, "next tasks");
	}

}
