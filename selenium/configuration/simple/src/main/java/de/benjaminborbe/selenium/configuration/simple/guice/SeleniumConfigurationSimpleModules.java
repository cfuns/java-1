package de.benjaminborbe.selenium.configuration.simple.guice;

import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class SeleniumConfigurationSimpleModules implements Modules {

	private final BundleContext context;

	public SeleniumConfigurationSimpleModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new SeleniumConfigurationSimpleOsgiModule(), new SeleniumConfigurationSimpleModule(), new ToolModule());
	}
}
