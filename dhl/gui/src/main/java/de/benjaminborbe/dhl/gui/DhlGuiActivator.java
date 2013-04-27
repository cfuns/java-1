package de.benjaminborbe.dhl.gui;

import de.benjaminborbe.dhl.gui.guice.DhlGuiModules;
import de.benjaminborbe.dhl.gui.service.DhlGuiNavigationEntry;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiCreateServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiDeleteServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiListServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiNotifyStatusServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiServlet;
import de.benjaminborbe.dhl.gui.servlet.DhlGuiTriggerCheckServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DhlGuiActivator extends HttpBundleActivator {

	@Inject
	private DhlGuiTriggerCheckServlet dhlGuiTriggerCheckServlet;

	@Inject
	private DhlGuiNavigationEntry dhlGuiNavigationEntry;

	@Inject
	private DhlGuiServlet dhlGuiServlet;

	@Inject
	private DhlGuiNotifyStatusServlet dhlGuiSendStatusServlet;

	@Inject
	private DhlGuiCreateServlet dhlGuiCreateServlet;

	@Inject
	private DhlGuiDeleteServlet dhlGuiDeleteServlet;

	@Inject
	private DhlGuiListServlet dhlGuiListServlet;

	public DhlGuiActivator() {
		super(DhlGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new DhlGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(dhlGuiServlet, DhlGuiConstants.URL_SLASH));
		result.add(new ServletInfo(dhlGuiSendStatusServlet, DhlGuiConstants.URL_NOTIFY_STATUS));
		result.add(new ServletInfo(dhlGuiCreateServlet, DhlGuiConstants.URL_CREATE));
		result.add(new ServletInfo(dhlGuiDeleteServlet, DhlGuiConstants.URL_DELETE));
		result.add(new ServletInfo(dhlGuiListServlet, DhlGuiConstants.URL_LIST));
		result.add(new ServletInfo(dhlGuiTriggerCheckServlet, DhlGuiConstants.URL_TRIGGER_CHECK));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, dhlGuiNavigationEntry));
		return result;
	}

}
