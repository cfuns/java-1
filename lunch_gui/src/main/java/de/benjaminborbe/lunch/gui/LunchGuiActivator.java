package de.benjaminborbe.lunch.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.lunch.gui.guice.LunchGuiModules;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiArchivServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiKioskBookingServlet;
import de.benjaminborbe.lunch.gui.servlet.LunchGuiServlet;
import de.benjaminborbe.lunch.gui.util.LunchGuiArchivNavigationEntry;
import de.benjaminborbe.lunch.gui.util.LunchGuiBookingNavigationEntry;
import de.benjaminborbe.lunch.gui.util.LunchGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class LunchGuiActivator extends HttpBundleActivator {

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

	public LunchGuiActivator() {
		super(LunchGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LunchGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(lunchGuiKioskBooking, LunchGuiConstants.URL_BOOKING));
		result.add(new ServletInfo(lunchGuiServlet, LunchGuiConstants.URL_HOME));
		result.add(new ServletInfo(lunchGuiArchivServlet, LunchGuiConstants.URL_ARCHIV));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo(LunchGuiConstants.URL_CSS, "css"));
		result.add(new ResourceInfo(LunchGuiConstants.URL_JS, "js"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiNavigationEntry));
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiArchivNavigationEntry));
		result.add(new ServiceInfo(NavigationEntry.class, lunchGuiBookingNavigationEntry));
		return result;
	}
}
