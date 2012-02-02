package de.benjaminborbe.slash;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.slash.guice.SlashModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class SlashActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SlashModules(context);
	}

}
