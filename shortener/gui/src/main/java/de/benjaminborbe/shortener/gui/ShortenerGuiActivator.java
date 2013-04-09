package de.benjaminborbe.shortener.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.shortener.gui.guice.ShortenerGuiModules;
import de.benjaminborbe.shortener.gui.servlet.ShortenerGuiRedirectServlet;
import de.benjaminborbe.shortener.gui.servlet.ShortenerGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ShortenerGuiActivator extends HttpBundleActivator {

	@Inject
	private ShortenerGuiRedirectServlet shortenerGuiRedirectServlet;

	@Inject
	private ShortenerGuiServlet shortenerGuiServlet;

	public ShortenerGuiActivator() {
		super(ShortenerGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ShortenerGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(shortenerGuiServlet, ShortenerGuiConstants.URL_HOME));
		result.add(new ServletInfo(shortenerGuiRedirectServlet, ShortenerGuiConstants.URL_REDIRECT, true));
		return result;
	}

}
