package de.benjaminborbe.wiki.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.wiki.gui.guice.WikiGuiModules;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiDashboardServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageCreateServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageDeleteServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageEditServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageShowServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WikiGuiActivator extends HttpBundleActivator {

	@Inject
	private WikiGuiDashboardServlet wikiGuiDashboardServlet;

	@Inject
	private WikiGuiPageEditServlet wikiGuiPageEditServlet;

	@Inject
	private WikiGuiPageCreateServlet wikiGuiPageCreateServlet;

	@Inject
	private WikiGuiPageShowServlet wikiGuiPageShowServlet;

	@Inject
	private WikiGuiPageDeleteServlet wikiGuiPageDeleteServlet;

	public WikiGuiActivator() {
		super("wiki");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WikiGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(wikiGuiDashboardServlet, "/"));
		result.add(new ServletInfo(wikiGuiPageEditServlet, "/edit"));
		result.add(new ServletInfo(wikiGuiPageCreateServlet, "/create"));
		result.add(new ServletInfo(wikiGuiPageShowServlet, "/show"));
		result.add(new ServletInfo(wikiGuiPageDeleteServlet, "/delete"));
		return result;
	}

	// @Override
	// protected Collection<FilterInfo> getFilterInfos() {
	// final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
	// result.add(new FilterInfo(wikiFilter, ".*", 998));
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
