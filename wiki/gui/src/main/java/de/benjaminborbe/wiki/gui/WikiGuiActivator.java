package de.benjaminborbe.wiki.gui;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
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
import de.benjaminborbe.wiki.gui.util.WikiGuiNavigationEntry;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	@Inject
	private WikiGuiNavigationEntry wikiGuiNavigationEntry;

	public WikiGuiActivator() {
		super(WikiGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WikiGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(wikiGuiDashboardServlet, WikiGuiConstants.WIKI_GUI_DASHBOARD_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiPageEditServlet, WikiGuiConstants.WIKI_GUI_PAGE_EDIT_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiPageCreateServlet, WikiGuiConstants.WIKI_GUI_PAGE_CREATE_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiPageShowServlet, WikiGuiConstants.WIKI_GUI_PAGE_SHOW_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiPageDeleteServlet, WikiGuiConstants.WIKI_GUI_PAGE_DELETE_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiPageListServlet, WikiGuiConstants.WIKI_GUI_PAGE_LIST_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiSpaceEditServlet, WikiGuiConstants.WIKI_GUI_SPACE_EDIT_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiSpaceCreateServlet, WikiGuiConstants.WIKI_GUI_SPACE_CREATE_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiSpaceDeleteServlet, WikiGuiConstants.WIKI_GUI_SPACE_DELETE_SERVLET_URL));
		result.add(new ServletInfo(wikiGuiSpaceListServlet, WikiGuiConstants.WIKI_GUI_SPACE_LIST_SERVLET_URL));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, wikiGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		return result;
	}
}
