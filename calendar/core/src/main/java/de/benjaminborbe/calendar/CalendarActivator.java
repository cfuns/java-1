package de.benjaminborbe.calendar;

import de.benjaminborbe.calendar.guice.CalendarModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class CalendarActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CalendarModules(context);
	}

}
