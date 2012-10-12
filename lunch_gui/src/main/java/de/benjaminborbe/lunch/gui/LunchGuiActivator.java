package de.benjaminborbe.lunch.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.lunch.gui.guice.LunchGuiModules;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiArchivServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class LunchGuiActivator extends HttpBundleActivator {

	@Inject
	private LunchGuiServlet lunchGuiServlet;

	@Inject
	private LunchGuiArchivServlet lunchGuiArchivServlet;

	public LunchGuiActivator() {
		super(LunchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LunchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(lunchGuiServlet, LunchGuiConstants.URL_HOME));
		result.add(new ServletInfo(lunchGuiArchivServlet, LunchGuiConstants.URL_ARCHIV));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(lunchFilter, ".*", 998));
	// return result;
	// }

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}
}
