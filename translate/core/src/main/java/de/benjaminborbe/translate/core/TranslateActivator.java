package de.benjaminborbe.translate.core;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.translate.core.guice.TranslateModules;
import org.osgi.framework.BundleContext;

public class TranslateActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TranslateModules(context);
	}

}
