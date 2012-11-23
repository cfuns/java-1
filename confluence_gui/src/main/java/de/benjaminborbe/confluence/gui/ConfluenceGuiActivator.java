package de.benjaminborbe.confluence.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.confluence.gui.guice.ConfluenceGuiModules;
import de.benjaminborbe.confluence.gui.servlet.ConfluenceGuiInstanceCreateServlet;
import de.benjaminborbe.confluence.gui.servlet.ConfluenceGuiInstanceDeleteServlet;
import de.benjaminborbe.confluence.gui.servlet.ConfluenceGuiInstanceListServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ConfluenceGuiActivator extends HttpBundleActivator {

	@Inject
	private ConfluenceGuiInstanceCreateServlet confluenceGuiCreateServlet;

	@Inject
	private ConfluenceGuiInstanceDeleteServlet confluenceGuiDeleteServlet;

	@Inject
	private ConfluenceGuiInstanceListServlet confluenceGuiListServlet;

	public ConfluenceGuiActivator() {
		super(ConfluenceGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfluenceGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(confluenceGuiCreateServlet, ConfluenceGuiConstants.URL_INSTANCE_CREATE));
		result.add(new ServletInfo(confluenceGuiDeleteServlet, ConfluenceGuiConstants.URL_INSTANCE_LIST));
		result.add(new ServletInfo(confluenceGuiListServlet, ConfluenceGuiConstants.URL_INSTANCE_DELETE));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(confluenceFilter, ".*", 998));
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
