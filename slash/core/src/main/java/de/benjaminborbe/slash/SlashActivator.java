package de.benjaminborbe.slash;

import de.benjaminborbe.slash.guice.SlashModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;

public class SlashActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SlashModules(context);
	}

}
