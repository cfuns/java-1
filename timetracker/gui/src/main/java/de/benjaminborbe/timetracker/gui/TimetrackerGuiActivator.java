package de.benjaminborbe.timetracker.gui;

import de.benjaminborbe.timetracker.gui.guice.TimetrackerGuiModules;
import de.benjaminborbe.timetracker.gui.servlet.TimetrackerGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TimetrackerGuiActivator extends HttpBundleActivator {

	@Inject
	private TimetrackerGuiServlet timetrackerGuiServlet;

	public TimetrackerGuiActivator() {
		super(TimetrackerGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TimetrackerGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(timetrackerGuiServlet, "/"));
		return result;
	}

}
