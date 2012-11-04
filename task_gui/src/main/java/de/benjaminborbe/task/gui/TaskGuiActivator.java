package de.benjaminborbe.task.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.task.gui.guice.TaskGuiModules;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskCompleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextDeleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextListServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskDeleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskUpdateServlet;
import de.benjaminborbe.task.gui.servlet.*;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTasksUncompletedServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskUncompleteServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class TaskGuiActivator extends HttpBundleActivator {

	@Inject
	private TaskGuiTaskSwapPrioServlet taskGuiTaskSwapPrioServlet;

	@Inject
	private TaskGuiTaskUpdateServlet taskGuiTaskUpdateServlet;

	@Inject
	private TaskGuiTaskContextCreateServlet taskGuiTaskContextCreateServlet;

	@Inject
	private TaskGuiTaskContextDeleteServlet taskGuiTaskContextDeleteServlet;

	@Inject
	private TaskGuiTaskContextListServlet taskGuiTaskContextListServlet;

	@Inject
	private TaskGuiTaskDeleteServlet taskGuiTaskDeleteServlet;

	@Inject
	private TaskGuiTaskCompleteServlet taskGuiCompleteServlet;

	@Inject
	private TaskGuiTaskUncompleteServlet taskGuiUncompleteServlet;

	@Inject
	private TaskGuiTaskCreateServlet taskGuiCreateServlet;

	@Inject
	private TaskGuiTasksUncompletedServlet taskGuiUncompletedTaskListServlet;

	@Inject
	private TaskGuiTasksCompletedServlet taskGuiCompletedTaskListServlet;

	public TaskGuiActivator() {
		super(TaskGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TaskGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(taskGuiTaskSwapPrioServlet, TaskGuiConstants.URL_TASK_SWAP_PRIO));
		result.add(new ServletInfo(taskGuiTaskDeleteServlet, TaskGuiConstants.URL_TASK_DELETE));
		result.add(new ServletInfo(taskGuiCreateServlet, TaskGuiConstants.URL_TASK_CREATE));
		result.add(new ServletInfo(taskGuiTaskUpdateServlet, TaskGuiConstants.URL_TASK_UPDATE));
		result.add(new ServletInfo(taskGuiUncompletedTaskListServlet, TaskGuiConstants.URL_TASKS_UNCOMPLETED));
		result.add(new ServletInfo(taskGuiCompletedTaskListServlet, TaskGuiConstants.URL_TASKS_COMPLETED));
		result.add(new ServletInfo(taskGuiCompleteServlet, TaskGuiConstants.URL_TASK_COMPLETE));
		result.add(new ServletInfo(taskGuiUncompleteServlet, TaskGuiConstants.URL_TASK_UNCOMPLETE));
		result.add(new ServletInfo(taskGuiTaskContextCreateServlet, TaskGuiConstants.URL_TASKCONTEXT_CREATE));
		result.add(new ServletInfo(taskGuiTaskContextDeleteServlet, TaskGuiConstants.URL_TASKCONTEXT_DELETE));
		result.add(new ServletInfo(taskGuiTaskContextListServlet, TaskGuiConstants.URL_TASKCONTEXT_LIST));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, new NavigationEntryImpl("Task", "/bb/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_UNCOMPLETED)));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(taskFilter, ".*", 998));
	// return result;
	// }

	// @Override
	// protected Collection<ResourceInfo> getResouceInfos() {
	// final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
	// // result.add(new ResourceInfo("/css", "css"));
	// // result.add(new ResourceInfo("/js", "js"));
	// return result;
	// }
}
