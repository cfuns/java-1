package de.benjaminborbe.timetracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.timetracker.guice.TimetrackerModules;
import de.benjaminborbe.timetracker.servlet.TimetrackerServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class TimetrackerActivator extends HttpBundleActivator {

	@Inject
	private TimetrackerServlet timetrackerServlet;

	public TimetrackerActivator() {
		super("timetracker");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TimetrackerModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(timetrackerServlet, "/"));
		return result;
	}
}
