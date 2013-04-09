package de.benjaminborbe.notification.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.notification.gui.guice.NotificationGuiModules;
import de.benjaminborbe.notification.gui.service.NotificationGuiNavigationEntry;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiAddServlet;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiListServlet;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiRemoveServlet;
import de.benjaminborbe.notification.gui.servlet.NotificationGuiSendServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NotificationGuiActivator extends HttpBundleActivator {

	@Inject
	private NotificationGuiAddServlet notificationGuiAddServlet;

	@Inject
	private NotificationGuiRemoveServlet notificationGuiRemoveServlet;

	@Inject
	private NotificationGuiNavigationEntry notificationGuiNavigationEntry;

	@Inject
	private NotificationGuiSendServlet notificationGuiSendServlet;

	@Inject
	private NotificationGuiListServlet notificationGuiListServlet;

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
		result.add(new ServletInfo(notificationGuiListServlet, NotificationGuiConstants.URL_LIST));
		result.add(new ServletInfo(notificationGuiSendServlet, NotificationGuiConstants.URL_SEND));
		result.add(new ServletInfo(notificationGuiAddServlet, NotificationGuiConstants.URL_ADD));
		result.add(new ServletInfo(notificationGuiRemoveServlet, NotificationGuiConstants.URL_REMOVE));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, notificationGuiNavigationEntry));
		return result;
	}

}
