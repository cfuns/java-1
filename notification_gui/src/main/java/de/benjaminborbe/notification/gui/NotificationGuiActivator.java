package de.benjaminborbe.notification.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.notification.gui.guice.NotificationGuiModules;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NotificationGuiActivator extends HttpBundleActivator {

	@Inject
	private NotificationGuiServlet notificationGuiServlet;

	public NotificationGuiActivator() {
		super(NotificationGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NotificationGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(notificationGuiServlet, NotificationGuiConstants.URL_HOME));
		return result;
	}

}
