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
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageListServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiPageShowServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiSpaceCreateServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiSpaceDeleteServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiSpaceEditServlet;
import de.benjaminborbe.wiki.gui.servlet.WikiGuiSpaceListServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class WikiGuiActivator extends HttpBundleActivator {

	@Inject
	private WikiGuiDashboardServlet wikiGuiDashboardServlet;

	@Inject
	private WikiGuiPageCreateServlet wikiGuiPageCreateServlet;

	@Inject
	private WikiGuiPageDeleteServlet wikiGuiPageDeleteServlet;

	@Inject
	private WikiGuiPageEditServlet wikiGuiPageEditServlet;

	@Inject
	private WikiGuiPageListServlet wikiGuiPageListServlet;

	@Inject
	private WikiGuiPageShowServlet wikiGuiPageShowServlet;

	@Inject
	private WikiGuiSpaceCreateServlet wikiGuiSpaceCreateServlet;

	@Inject
	private WikiGuiSpaceDeleteServlet wikiGuiSpaceDeleteServlet;

	@Inject
	private WikiGuiSpaceEditServlet wikiGuiSpaceEditServlet;

	@Inject
	private WikiGuiSpaceListServlet wikiGuiSpaceListServlet;

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
		result.add(new ServletInfo(wikiGuiPageEditServlet, "/page/edit"));
		result.add(new ServletInfo(wikiGuiPageCreateServlet, "/page/create"));
		result.add(new ServletInfo(wikiGuiPageShowServlet, "/page/show"));
		result.add(new ServletInfo(wikiGuiPageDeleteServlet, "/page/delete"));
		result.add(new ServletInfo(wikiGuiPageListServlet, "/page/list"));
		result.add(new ServletInfo(wikiGuiSpaceEditServlet, "/space/edit"));
		result.add(new ServletInfo(wikiGuiSpaceCreateServlet, "/space/create"));
		result.add(new ServletInfo(wikiGuiSpaceDeleteServlet, "/space/delete"));
		result.add(new ServletInfo(wikiGuiSpaceListServlet, "/space/list"));
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
