package de.benjaminborbe.cache.gui;

import javax.inject.Inject;
import de.benjaminborbe.cache.gui.guice.CacheGuiModules;
import de.benjaminborbe.cache.gui.servlet.CacheGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CacheGuiActivator extends HttpBundleActivator {

	@Inject
	private CacheGuiServlet cacheGuiServlet;

	public CacheGuiActivator() {
		super(CacheGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CacheGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(cacheGuiServlet, CacheGuiConstants.URL_HOME));
		return result;
	}

}
