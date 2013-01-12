package de.benjaminborbe.geocaching.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.geocaching.gui.guice.GeocachingGuiModules;
import de.benjaminborbe.geocaching.gui.servlet.GeocachingGuiCurrentLocationOnGoogleMapsServlet;
import de.benjaminborbe.geocaching.gui.servlet.GeocachingGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class GeocachingGuiActivator extends HttpBundleActivator {

	@Inject
	private GeocachingGuiServlet geocachingGuiServlet;

	@Inject
	private GeocachingGuiCurrentLocationOnGoogleMapsServlet geocachingGuiCurrentLocationOnGoogleMapsServlet;

	public GeocachingGuiActivator() {
		super(GeocachingGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GeocachingGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(geocachingGuiServlet, "/"));
		result.add(new ServletInfo(geocachingGuiCurrentLocationOnGoogleMapsServlet, "/loc"));
		return result;
	}

}
