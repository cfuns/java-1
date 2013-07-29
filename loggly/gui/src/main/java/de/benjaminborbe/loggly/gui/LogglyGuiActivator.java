package de.benjaminborbe.loggly.gui;

import de.benjaminborbe.loggly.gui.guice.LogglyGuiModules;
import de.benjaminborbe.loggly.gui.servlet.LogglyGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LogglyGuiActivator extends HttpBundleActivator {

	@Inject
	private LogglyGuiServlet logglyGuiServlet;

	public LogglyGuiActivator() {
		super(LogglyGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LogglyGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(logglyGuiServlet, LogglyGuiConstants.URL_HOME));
		return result;
	}

}
