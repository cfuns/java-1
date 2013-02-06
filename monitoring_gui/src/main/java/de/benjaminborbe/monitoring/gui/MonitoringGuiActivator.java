package de.benjaminborbe.monitoring.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModules;
import de.benjaminborbe.monitoring.gui.service.MonitoringGuiNavigationEntry;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeCheckServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeCreateServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeDeleteServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeJsonServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeListJsonServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeListServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeSilentServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiNodeUpdateServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiTriggerCheckServlet;
import de.benjaminborbe.monitoring.gui.servlet.MonitoringGuiTriggerMailServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MonitoringGuiActivator extends HttpBundleActivator {

	@Inject
	private MonitoringGuiNodeCheckServlet monitoringGuiNodeCheckServlet;

	@Inject
	private MonitoringGuiNodeJsonServlet monitoringGuiNodeJsonServlet;

	@Inject
	private MonitoringGuiNodeSilentServlet monitoringGuiNodeSilentServlet;

	@Inject
	private MonitoringGuiTriggerCheckServlet monitoringGuiTriggerCheckServlet;

	@Inject
	private MonitoringGuiTriggerMailServlet monitoringGuiTriggerMailServlet;

	@Inject
	private MonitoringGuiNodeListServlet monitoringGuiNodeListServlet;

	@Inject
	private MonitoringGuiServlet monitoringGuiServlet;

	@Inject
	private MonitoringGuiNodeCreateServlet monitoringGuiNodeCreateServlet;

	@Inject
	private MonitoringGuiNodeDeleteServlet monitoringGuiNodeDeleteServlet;

	@Inject
	private MonitoringGuiNodeUpdateServlet monitoringGuiNodeUpdateServlet;

	@Inject
	private MonitoringGuiNavigationEntry monitoringGuiNavigationEntry;

	@Inject
	private MonitoringGuiNodeListJsonServlet monitoringGuiNodeListJsonServlet;

	public MonitoringGuiActivator() {
		super(MonitoringGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MonitoringGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, monitoringGuiNavigationEntry, monitoringGuiNavigationEntry.getClass().getName()));

		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(monitoringGuiServlet, MonitoringGuiConstants.URL_HOME));
		result.add(new ServletInfo(monitoringGuiNodeCheckServlet, MonitoringGuiConstants.URL_NODE_CHECK));
		result.add(new ServletInfo(monitoringGuiNodeJsonServlet, MonitoringGuiConstants.URL_NODE_JSON));
		result.add(new ServletInfo(monitoringGuiNodeListServlet, MonitoringGuiConstants.URL_NODE_LIST));
		result.add(new ServletInfo(monitoringGuiNodeListJsonServlet, MonitoringGuiConstants.URL_NODE_LIST_JSON));
		result.add(new ServletInfo(monitoringGuiNodeCreateServlet, MonitoringGuiConstants.URL_NODE_CREATE));
		result.add(new ServletInfo(monitoringGuiNodeDeleteServlet, MonitoringGuiConstants.URL_NODE_DELETE));
		result.add(new ServletInfo(monitoringGuiNodeUpdateServlet, MonitoringGuiConstants.URL_NODE_UPDATE));
		result.add(new ServletInfo(monitoringGuiNodeSilentServlet, MonitoringGuiConstants.URL_NODE_SILENT));
		result.add(new ServletInfo(monitoringGuiTriggerCheckServlet, MonitoringGuiConstants.URL_TRIGGER_CHECK));
		result.add(new ServletInfo(monitoringGuiTriggerMailServlet, MonitoringGuiConstants.URL_TRIGGER_MAIL));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}

}
