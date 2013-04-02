package de.benjaminborbe.proxy.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.proxy.gui.guice.ProxyGuiModules;
import de.benjaminborbe.proxy.gui.servlet.ProxyGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class ProxyGuiActivator extends HttpBundleActivator {

	@Inject
	private ProxyGuiServlet proxyGuiServlet;

	public ProxyGuiActivator() {
		super(ProxyGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProxyGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(proxyGuiServlet, ProxyGuiConstants.URL_HOME));
		return result;
	}

}
