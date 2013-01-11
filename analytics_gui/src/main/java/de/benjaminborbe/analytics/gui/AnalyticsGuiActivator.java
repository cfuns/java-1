package de.benjaminborbe.analytics.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.gui.guice.AnalyticsGuiModules;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportAddDataServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportListServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportTableServlet;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AnalyticsGuiActivator extends HttpBundleActivator {

	@Inject
	private AnalyticsGuiNavigationEntry analyticsGuiNavigationEntry;

	@Inject
	private AnalyticsGuiReportAddDataServlet analyticsGuiAddDataServlet;

	@Inject
	private AnalyticsGuiReportTableServlet analyticsGuiTableServlet;

	@Inject
	private AnalyticsGuiReportListServlet analyticsGuiServlet;

	public AnalyticsGuiActivator() {
		super(AnalyticsGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new AnalyticsGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(analyticsGuiServlet, AnalyticsGuiConstants.URL_REPORT_LIST));
		result.add(new ServletInfo(analyticsGuiTableServlet, AnalyticsGuiConstants.URL_REPORT_TABLE));
		result.add(new ServletInfo(analyticsGuiAddDataServlet, AnalyticsGuiConstants.URL_REPORT_CREATE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, analyticsGuiNavigationEntry));
		return result;
	}
}
