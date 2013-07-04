package de.benjaminborbe.timetracker;

import de.benjaminborbe.timetracker.guice.TimetrackerModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class TimetrackerActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TimetrackerModules(context);
	}

}
