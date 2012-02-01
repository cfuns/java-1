package de.benjaminborbe.calendar;

import org.osgi.framework.BundleContext;
import de.benjaminborbe.calendar.guice.CalendarModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class CalendarActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CalendarModules(context);
	}

}
