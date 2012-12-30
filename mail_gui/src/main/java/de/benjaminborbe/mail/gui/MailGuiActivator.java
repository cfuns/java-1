package de.benjaminborbe.mail.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.mail.gui.guice.MailGuiModules;
import de.benjaminborbe.mail.gui.service.MailGuiDashboardWidget;
import de.benjaminborbe.mail.gui.servlet.MailGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MailGuiActivator extends HttpBundleActivator {

	@Inject
	private MailGuiServlet mailGuiServlet;

	@Inject
	private MailGuiDashboardWidget mailDashboardWidget;

	public MailGuiActivator() {
		super(MailGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MailGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, mailDashboardWidget, mailDashboardWidget.getClass().getName()));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(mailGuiServlet, MailGuiConstants.URL_HOME));
		return result;
	}

}
