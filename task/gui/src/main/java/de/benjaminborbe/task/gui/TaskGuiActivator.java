package de.benjaminborbe.task.gui;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.task.gui.guice.TaskGuiModules;
import de.benjaminborbe.task.gui.service.TaskGuiDashboardWidget;
import de.benjaminborbe.task.gui.service.TaskGuiSpecialSearch;
import de.benjaminborbe.task.gui.servlet.TaskGuiNextServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskAttachmentCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskAttachmentDeleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskCompleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextDeleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextListServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextUpdateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextUserRemoveServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskContextUserServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskDeleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskFirstServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskLastServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskSelectTaskContextServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskStartLaterServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskSwapPrioServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskUncompleteServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskUpdateFocusServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskUpdateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTaskViewServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTasksCompletedServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiTasksUncompletedServlet;
import de.benjaminborbe.task.gui.util.TaskGuiNavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TaskGuiActivator extends HttpBundleActivator {

	@Inject
	private TaskGuiTaskAttachmentCreateServlet taskGuiTaskAttachmentCreateServlet;

	@Inject
	private TaskGuiTaskAttachmentDeleteServlet taskGuiTaskAttachmentDeleteServlet;

	@Inject
	private TaskGuiTaskSelectTaskContextServlet taskGuiTaskSelectTaskContextServlet;

	@Inject
	private TaskGuiTaskUpdateFocusServlet taskGuiTaskUpdateFocusServlet;

	@Inject
	private TaskGuiTaskContextUserRemoveServlet taskGuiTaskContextUserRemoveServlet;

	@Inject
	private TaskGuiTaskContextUserServlet taskGuiTaskContextUserServlet;

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

	@Inject
	private TaskGuiNavigationEntry taskGuiNavigationEntry;

	public TaskGuiActivator() {
		super(TaskGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TaskGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());

		// task
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
		result.add(new ServletInfo(taskGuiTaskViewServlet, TaskGuiConstants.URL_TASK_VIEW));
		result.add(new ServletInfo(taskGuiTaskUpdateFocusServlet, TaskGuiConstants.URL_TASK_UPDATE_FOCUS));
		result.add(new ServletInfo(taskGuiTaskSelectTaskContextServlet, TaskGuiConstants.URL_TASK_SELECT_TASKCONTEXT));

		// context
		result.add(new ServletInfo(taskGuiTaskContextUserRemoveServlet, TaskGuiConstants.URL_TASKCONTEXT_USER_REMOVE));
		result.add(new ServletInfo(taskGuiTaskContextUserServlet, TaskGuiConstants.URL_TASKCONTEXT_USER));
		result.add(new ServletInfo(taskGuiTaskContextUpdateServlet, TaskGuiConstants.URL_TASKCONTEXT_UPDATE));
		result.add(new ServletInfo(taskGuiTaskContextCreateServlet, TaskGuiConstants.URL_TASKCONTEXT_CREATE));
		result.add(new ServletInfo(taskGuiTaskContextDeleteServlet, TaskGuiConstants.URL_TASKCONTEXT_DELETE));
		result.add(new ServletInfo(taskGuiTaskContextListServlet, TaskGuiConstants.URL_TASKCONTEXT_LIST));

		// attachment
		result.add(new ServletInfo(taskGuiTaskAttachmentDeleteServlet, TaskGuiConstants.URL_TASKATTACHMENT_DELETE));
		result.add(new ServletInfo(taskGuiTaskAttachmentCreateServlet, TaskGuiConstants.URL_TASKATTACHMENT_CREATE));

		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, taskGuiNavigationEntry));
		result.add(new ServiceInfo(DashboardContentWidget.class, taskGuiDashboardWidget, taskGuiDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, taskGuiSpecialSearch, taskGuiSpecialSearch.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo(TaskGuiConstants.URL_CSS, "css"));
		result.add(new ResourceInfo(TaskGuiConstants.URL_JS, "js"));
		result.add(new ResourceInfo(TaskGuiConstants.URL_IMAGES, "images"));
		return result;
	}

}
