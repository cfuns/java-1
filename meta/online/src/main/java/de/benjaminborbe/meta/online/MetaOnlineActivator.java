package de.benjaminborbe.meta.online;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.meta.online.guice.MetaOnlineModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;

public class MetaOnlineActivator extends BaseBundleActivator {

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MetaOnlineModules(context);
	}

}
