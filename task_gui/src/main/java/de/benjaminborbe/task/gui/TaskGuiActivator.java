package de.benjaminborbe.task.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntryImpl;
import de.benjaminborbe.task.gui.guice.TaskGuiModules;
import de.benjaminborbe.task.gui.servlet.TaskGuiCreateServlet;
import de.benjaminborbe.task.gui.servlet.TaskGuiNextServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class TaskGuiActivator extends HttpBundleActivator {

	@Inject
	private TaskGuiCreateServlet taskGuiCreateServlet;

	@Inject
	private TaskGuiNextServlet taskGuiNextServlet;

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
		result.add(new ServletInfo(taskGuiCreateServlet, TaskGuiConstants.URL_CREATE));
		result.add(new ServletInfo(taskGuiNextServlet, TaskGuiConstants.URL_NEXT));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, new NavigationEntryImpl("Task", "/bb/" + TaskGuiConstants.NAME)));
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
