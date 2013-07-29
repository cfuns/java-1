package de.benjaminborbe.streamcache.gui;

import de.benjaminborbe.streamcache.gui.guice.StreamcacheGuiModules;
import de.benjaminborbe.streamcache.gui.servlet.StreamcacheGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StreamcacheGuiActivator extends HttpBundleActivator {

	@Inject
	private StreamcacheGuiServlet streamcacheGuiServlet;

	public StreamcacheGuiActivator() {
		super(StreamcacheGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StreamcacheGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(streamcacheGuiServlet, "/"));
		return result;
	}

}
