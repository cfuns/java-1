package de.benjaminborbe.lunch.gui;

import javax.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.lunch.gui.config.LunchGuiConfig;
import de.benjaminborbe.lunch.gui.guice.LunchGuiModules;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiArchivServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiKioskBookedServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiKioskBookingServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiNotificationActivateJsonServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiNotificationDeactivateJsonServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiNotificationIsActivatedJsonServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiNotificationServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiServlet;
import de.benjaminborbe.lunch.gui.util.LunchGuiArchivNavigationEntry;
import de.benjaminborbe.lunch.gui.util.LunchGuiBookingNavigationEntry;
import de.benjaminborbe.lunch.gui.util.LunchGuiNavigationEntry;
import de.benjaminborbe.lunch.gui.util.LunchGuiNotificationNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LunchGuiActivator extends HttpBundleActivator {

	@Inject
	private LunchGuiNotificationServlet lunchGuiNotificationServlet;

	@Inject
	private LunchGuiConfig lunchGuiConfig;

	@Inject
	private LunchGuiNotificationActivateJsonServlet lunchGuiNotificationActivate;

	@Inject
	private LunchGuiNotificationDeactivateJsonServlet lunchGuiNotificationDeactivate;

	@Inject
	private LunchGuiNotificationIsActivatedJsonServlet lunchGuiNotificationIsActivated;

	@Inject
	private LunchGuiKioskBookedServlet lunchGuiKioskBookedServlet;

	@Inject
	private LunchGuiKioskBookingServlet lunchGuiKioskBooking;

	@Inject
	private LunchGuiServlet lunchGuiServlet;

	@Inject
	private LunchGuiArchivServlet lunchGuiArchivServlet;

	@Inject
	private LunchGuiArchivNavigationEntry lunchGuiArchivNavigationEntry;

	@Inject
	private LunchGuiNavigationEntry lunchGuiNavigationEntry;

	@Inject
	private LunchGuiBookingNavigationEntry lunchGuiBookingNavigationEntry;

	@Inject
	private LunchGuiNotificationNavigationEntry lunchGuiNotificationNavigationEntry;

	public LunchGuiActivator() {
		super(LunchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LunchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(lunchGuiKioskBookedServlet, LunchGuiConstants.URL_BOOKED));
		result.add(new ServletInfo(lunchGuiKioskBooking, LunchGuiConstants.URL_BOOKING));
		result.add(new ServletInfo(lunchGuiServlet, LunchGuiConstants.URL_HOME));
		result.add(new ServletInfo(lunchGuiArchivServlet, LunchGuiConstants.URL_ARCHIV));
		result.add(new ServletInfo(lunchGuiNotificationServlet, LunchGuiConstants.URL_NOTIFICATION));
		result.add(new ServletInfo(lunchGuiNotificationActivate, LunchGuiConstants.URL_NOTIFICATION_ACTIVATE));
		result.add(new ServletInfo(lunchGuiNotificationDeactivate, LunchGuiConstants.URL_NOTIFICATION_DEACTIVATE));
		result.add(new ServletInfo(lunchGuiNotificationIsActivated, LunchGuiConstants.URL_NOTIFICATION_ISACTIVATED));

		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo(LunchGuiConstants.URL_CSS, "css"));
		result.add(new ResourceInfo(LunchGuiConstants.URL_JS, "js"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiNotificationNavigationEntry));
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiNavigationEntry));
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiArchivNavigationEntry));
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiBookingNavigationEntry));
		for (final ConfigurationDescription configuration : lunchGuiConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}
}
