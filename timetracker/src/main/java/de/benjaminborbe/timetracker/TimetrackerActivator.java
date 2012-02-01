package de.benjaminborbe.timetracker;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.timetracker.guice.TimetrackerModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class TimetrackerActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TimetrackerModules(context);
	}

}
