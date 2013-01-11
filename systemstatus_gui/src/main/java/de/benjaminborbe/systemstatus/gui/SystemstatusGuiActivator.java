package de.benjaminborbe.systemstatus.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.systemstatus.gui.guice.SystemstatusGuiModules;
import de.benjaminborbe.systemstatus.gui.servlet.SystemstatusGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class SystemstatusGuiActivator extends HttpBundleActivator {

	@Inject
	private SystemstatusGuiServlet systemstatusGuiServlet;

	public SystemstatusGuiActivator() {
		super(SystemstatusGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SystemstatusGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(systemstatusGuiServlet, "/"));
		return result;
	}

}
