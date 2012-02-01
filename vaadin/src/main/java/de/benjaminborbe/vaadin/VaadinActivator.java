package de.benjaminborbe.vaadin;

import org.osgi.framework.BundleContext;
import de.benjaminborbe.vaadin.guice.VaadinModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class VaadinActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VaadinModules(context);
	}

}
