package de.benjaminborbe.checklist.gui;

import javax.inject.Inject;
import de.benjaminborbe.checklist.gui.guice.ChecklistGuiModules;
import de.benjaminborbe.checklist.gui.service.ChecklistGuiNavigationEntry;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryCompleteServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryCreateServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryDeleteServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryListServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryUncompleteServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiEntryUpdateServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiListCreateServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiListDeleteServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiListListServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiListResetServlet;
import de.benjaminborbe.checklist.gui.servlet.ChecklistGuiListUpdateServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ChecklistGuiActivator extends HttpBundleActivator {

	@Inject
	private ChecklistGuiListResetServlet checklistGuiListResetServlet;

	@Inject
	private ChecklistGuiEntryCreateServlet checklistGuiEntryCreateServlet;

	@Inject
	private ChecklistGuiEntryDeleteServlet checklistGuiEntryDeleteServlet;

	@Inject
	private ChecklistGuiEntryListServlet checklistGuiEntryListServlet;

	@Inject
	private ChecklistGuiEntryUpdateServlet checklistGuiEntryUpdateServlet;

	@Inject
	private ChecklistGuiListCreateServlet checklistGuiListCreateServlet;

	@Inject
	private ChecklistGuiListDeleteServlet checklistGuiListDeleteServlet;

	@Inject
	private ChecklistGuiListListServlet checklistGuiListEntryServlet;

	@Inject
	private ChecklistGuiListUpdateServlet checklistGuiListUpdateServlet;

	@Inject
	private ChecklistGuiNavigationEntry checklistGuiNavigationEntry;

	@Inject
	private ChecklistGuiEntryCompleteServlet checklistGuiEntryCompleteServlet;

	@Inject
	private ChecklistGuiEntryUncompleteServlet checklistGuiEntryUncompleteServlet;

	public ChecklistGuiActivator() {
		super(ChecklistGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ChecklistGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, checklistGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(checklistGuiListResetServlet, ChecklistGuiConstants.URL_LIST_RESET));
		result.add(new ServletInfo(checklistGuiEntryCreateServlet, ChecklistGuiConstants.URL_ENTRY_CREATE));
		result.add(new ServletInfo(checklistGuiEntryDeleteServlet, ChecklistGuiConstants.URL_ENTRY_DELETE));
		result.add(new ServletInfo(checklistGuiEntryListServlet, ChecklistGuiConstants.URL_ENTRY_LIST));
		result.add(new ServletInfo(checklistGuiEntryUpdateServlet, ChecklistGuiConstants.URL_ENTRY_UPDATE));
		result.add(new ServletInfo(checklistGuiListCreateServlet, ChecklistGuiConstants.URL_LIST_CREATE));
		result.add(new ServletInfo(checklistGuiListDeleteServlet, ChecklistGuiConstants.URL_LIST_DELETE));
		result.add(new ServletInfo(checklistGuiListEntryServlet, ChecklistGuiConstants.URL_LIST_LIST));
		result.add(new ServletInfo(checklistGuiListUpdateServlet, ChecklistGuiConstants.URL_LIST_UPDATE));
		result.add(new ServletInfo(checklistGuiEntryCompleteServlet, ChecklistGuiConstants.URL_ENTRY_COMPLETE));
		result.add(new ServletInfo(checklistGuiEntryUncompleteServlet, ChecklistGuiConstants.URL_ENTRY_UNCOMPLETE));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo(ChecklistGuiConstants.URL_IMAGES, "images"));
		result.add(new ResourceInfo(ChecklistGuiConstants.URL_CSS, "css"));
		return result;
	}
}
