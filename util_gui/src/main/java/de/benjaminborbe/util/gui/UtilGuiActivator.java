package de.benjaminborbe.util.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.util.gui.guice.UtilGuiModules;
import de.benjaminborbe.util.gui.servlet.UtilGuiPasswordGeneratorServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class UtilGuiActivator extends HttpBundleActivator {

	@Inject
	private UtilGuiServlet utilServlet;

	@Inject
	private UtilGuiPasswordGeneratorServlet utilPasswordGeneratorServlet;

	public UtilGuiActivator() {
		super("util");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(utilServlet, "/"));
		result.add(new ServletInfo(utilPasswordGeneratorServlet, "/passwordGenerator"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(utilFilter, ".*", 1));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		// result.add(new ResourceInfo("/css", "css"));
		// result.add(new ResourceInfo("/js", "js"));
		return result;
	}
}