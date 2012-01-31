package de.benjaminborbe.worktime.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.worktime.gui.guice.WorktimeGuiModules;
import de.benjaminborbe.worktime.gui.servlet.WorktimeGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WorktimeGuiActivator extends HttpBundleActivator {

	@Inject
	private WorktimeGuiServlet worktimeGuiServlet;

	public WorktimeGuiActivator() {
		super("worktime");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WorktimeGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(worktimeGuiServlet, "/"));
		return result;
	}

}
