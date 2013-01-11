package de.benjaminborbe.analytics.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.gui.guice.AnalyticsGuiModules;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiAddDataServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiServlet;
import de.benjaminborbe.analytics.gui.servlet.AnalyticsGuiTableServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class AnalyticsGuiActivator extends HttpBundleActivator {

	@Inject
	private AnalyticsGuiAddDataServlet analyticsGuiAddDataServlet;

	@Inject
	private AnalyticsGuiTableServlet analyticsGuiTableServlet;

	@Inject
	private AnalyticsGuiServlet analyticsGuiServlet;

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
		result.add(new ServletInfo(analyticsGuiServlet, AnalyticsGuiConstants.URL_HOME));
		result.add(new ServletInfo(analyticsGuiTableServlet, AnalyticsGuiConstants.URL_TABLE));
		result.add(new ServletInfo(analyticsGuiAddDataServlet, AnalyticsGuiConstants.URL_ADD_DATA));
		return result;
	}

}
