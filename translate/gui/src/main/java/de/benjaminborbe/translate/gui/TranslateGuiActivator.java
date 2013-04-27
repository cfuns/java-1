package de.benjaminborbe.translate.gui;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.translate.gui.guice.TranslateGuiModules;
import de.benjaminborbe.translate.gui.service.TranslateGuiDashboardWidget;
import de.benjaminborbe.translate.gui.service.TranslateGuiSpecialSearch;
import de.benjaminborbe.translate.gui.servlet.TranslateGuiServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TranslateGuiActivator extends HttpBundleActivator {

	@Inject
	private TranslateGuiServlet translateGuiServlet;

	@Inject
	private TranslateGuiDashboardWidget translateDashboardWidget;

	@Inject
	private TranslateGuiSpecialSearch translateGuiSpecialSearch;

	public TranslateGuiActivator() {
		super(TranslateGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TranslateGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(translateGuiServlet, "/"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, translateDashboardWidget, translateDashboardWidget.getClass().getName()));
		result.add(new ServiceInfo(SearchSpecial.class, translateGuiSpecialSearch, translateGuiSpecialSearch.getClass().getName()));
		return result;
	}

}
