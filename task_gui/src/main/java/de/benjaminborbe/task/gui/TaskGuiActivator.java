package de.benjaminborbe.task.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.task.gui.guice.TaskGuiModules;
import de.benjaminborbe.task.gui.service.TaskGuiDashboardWidget;
import de.benjaminborbe.task.gui.service.TaskGuiSpecialSearch;
import de.benjaminborbe.task.gui.servlet.*;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class TaskGuiActivator extends HttpBundleActivator {

	@Inject
	private TaskGuiTaskViewServlet taskGuiTaskViewServlet;

	@Inject
	private TaskGuiTaskSwapPrioServlet taskGuiTaskSwapPrioServlet;

	@Inject
	private TaskGuiTaskUpdateServlet taskGuiTaskUpdateServlet;

	@Inject
	private TaskGuiTaskStartLaterServlet taskGuiTaskStartLaterServlet;

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
	private TaskGuiNextServlet taskGuiNextServlet;

	@Inject
	private TaskGuiTasksCompletedServlet taskGuiCompletedTaskListServlet;

	@Inject
	private TaskGuiDashboardWidget taskGuiDashboardWidget;

	@Inject
	private TaskGuiTaskFirstServlet taskGuiTaskFirstServlet;

	@Inject
	private TaskGuiTaskLastServlet taskGuiTaskLastServlet;

	@Inject
	private TaskGuiTaskContextUpdateServlet taskGuiTaskContextUpdateServlet;

	@Inject
	private TaskGuiSpecialSearch taskGuiSpecialSearch;

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
		result.add(new ServletInfo(taskGuiTaskFirstServlet, TaskGuiConstants.URL_TASK_FIRST));
		result.add(new ServletInfo(taskGuiTaskLastServlet, TaskGuiConstants.URL_TASK_LAST));
		result.add(new ServletInfo(taskGuiTaskStartLaterServlet, TaskGuiConstants.URL_TASK_START_TOMORROW));
		result.add(new ServletInfo(taskGuiNextServlet, TaskGuiConstants.URL_TASKS_NEXT));
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
		result.add(new ServletInfo(taskGuiTaskViewServlet, TaskGuiConstants.URL_TASK_VIEW));
		result.add(new ServletInfo(taskGuiTaskContextUpdateServlet, TaskGuiConstants.URL_TASKCONTEXT_UPDATE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, new NavigationEntryImpl("Task", "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT)));
		result.add(new ServiceInfo(DashboardContentWidget.class, taskGuiDashboardWidget, taskGuiDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, taskGuiSpecialSearch, taskGuiSpecialSearch.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}

}
