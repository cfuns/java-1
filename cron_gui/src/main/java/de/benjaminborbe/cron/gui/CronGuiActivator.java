package de.benjaminborbe.cron.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.cron.gui.guice.CronGuiModules;
import de.benjaminborbe.cron.gui.service.CronGuiNavigationEntry;
import de.benjaminborbe.cron.gui.servlet.CronGuiLatestExecutedServlet;
import de.benjaminborbe.cron.gui.servlet.CronGuiListServlet;
import de.benjaminborbe.cron.gui.servlet.CronGuiManageServlet;
import de.benjaminborbe.cron.gui.servlet.CronGuiServlet;
import de.benjaminborbe.cron.gui.servlet.CronGuiTriggerServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class CronGuiActivator extends HttpBundleActivator {

	@Inject
	private CronGuiNavigationEntry cronGuiNavigationEntry;

	@Inject
	private CronGuiListServlet cronGuiListServlet;

	@Inject
	private CronGuiTriggerServlet cronGuiTriggerServlet;

	@Inject
	private CronGuiManageServlet cronGuiManageServlet;

	@Inject
	private CronGuiServlet cronGuiServlet;

	@Inject
	private CronGuiLatestExecutedServlet cronGuiLatestExecutedServlet;

	public CronGuiActivator() {
		super(CronGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CronGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());

		result.add(new ServletInfo(cronGuiListServlet, CronGuiConstants.URL_LIST));
		result.add(new ServletInfo(cronGuiTriggerServlet, CronGuiConstants.URL_CRON_TRIGGER));

		result.add(new ServletInfo(cronGuiServlet, CronGuiConstants.URL_HOME));
		result.add(new ServletInfo(cronGuiLatestExecutedServlet, CronGuiConstants.URL_LATEST));
		result.add(new ServletInfo(cronGuiManageServlet, CronGuiConstants.URL_MANAGE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, cronGuiNavigationEntry));
		return result;
	}

}
