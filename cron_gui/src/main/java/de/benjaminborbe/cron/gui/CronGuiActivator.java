package de.benjaminborbe.cron.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.cron.gui.guice.CronGuiModules;
import de.benjaminborbe.cron.gui.servlet.CronGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class CronGuiActivator extends HttpBundleActivator {

	@Inject
	private CronGuiServlet cronGuiServlet;

	public CronGuiActivator() {
		super("cron");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CronGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(cronGuiServlet, "/"));
		return result;
	}

}
