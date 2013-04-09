package de.benjaminborbe.translate;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.translate.guice.TranslateModules;

public class TranslateActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new TranslateModules(context);
	}

}
