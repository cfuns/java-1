package de.benjaminborbe.gwt.core;

import de.benjaminborbe.gwt.core.guice.GwtCoreModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class GwtCoreActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GwtCoreModules(context);
	}

}
