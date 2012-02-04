package de.benjaminborbe.translate.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.translate.gui.guice.TranslateGuiModules;
import de.benjaminborbe.translate.gui.service.TranslateGuiDashboardWidget;
import de.benjaminborbe.translate.gui.servlet.TranslateGuiServlet;

public class TranslateGuiActivator extends HttpBundleActivator {

	@Inject
	private TranslateGuiServlet translateGuiServlet;

	@Inject
	private TranslateGuiDashboardWidget translateDashboardWidget;

	public TranslateGuiActivator() {
		super("translate");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TranslateGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(translateGuiServlet, "/"));
		return result;
	}

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, translateDashboardWidget, translateDashboardWidget.getClass().getName()));
		return result;
	}

}
