package de.benjaminborbe.analytics.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.gui.guice.AnalyticsGuiModules;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportAddDataServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportAggregateServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportCreateServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportDeleteServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportListServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiReportViewServlet;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AnalyticsGuiActivator extends HttpBundleActivator {

	@Inject
	private AnalyticsGuiReportDeleteServlet analyticsGuiReportDeleteServlet;

	@Inject
	private AnalyticsGuiReportCreateServlet analyticsGuiReportCreateServlet;

	@Inject
	private AnalyticsGuiNavigationEntry analyticsGuiNavigationEntry;

	@Inject
	private AnalyticsGuiReportAddDataServlet analyticsGuiAddDataServlet;

	@Inject
	private AnalyticsGuiReportViewServlet analyticsGuiTableServlet;

	@Inject
	private AnalyticsGuiReportListServlet analyticsGuiServlet;

	@Inject
	private AnalyticsGuiReportAggregateServlet analyticsGuiReportAggregateServlet;

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
		result.add(new ServletInfo(analyticsGuiTableServlet, AnalyticsGuiConstants.URL_REPORT_VIEW));
		result.add(new ServletInfo(analyticsGuiAddDataServlet, AnalyticsGuiConstants.URL_REPORT_ADD_DATA));
		result.add(new ServletInfo(analyticsGuiReportCreateServlet, AnalyticsGuiConstants.URL_REPORT_CREATE));
		result.add(new ServletInfo(analyticsGuiReportDeleteServlet, AnalyticsGuiConstants.URL_REPORT_DELETE));
		result.add(new ServletInfo(analyticsGuiReportAggregateServlet, AnalyticsGuiConstants.URL_REPORT_AGGREGATE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, analyticsGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/css", "css"));
		return result;
	}
}
