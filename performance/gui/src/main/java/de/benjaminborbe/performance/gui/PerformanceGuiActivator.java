package de.benjaminborbe.performance.gui;

import de.benjaminborbe.performance.gui.guice.PerformanceGuiModules;
import de.benjaminborbe.performance.gui.servlet.PerformanceGuiFilter;
import de.benjaminborbe.performance.gui.servlet.PerformanceGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PerformanceGuiActivator extends HttpBundleActivator {

	@Inject
	private PerformanceGuiServlet performanceServlet;

	@Inject
	private PerformanceGuiFilter performanceFilter;

	public PerformanceGuiActivator() {
		super(PerformanceGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PerformanceGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(performanceServlet, "/"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		result.add(new FilterInfo(performanceFilter, ".*", 998, true));
		return result;
	}

}
