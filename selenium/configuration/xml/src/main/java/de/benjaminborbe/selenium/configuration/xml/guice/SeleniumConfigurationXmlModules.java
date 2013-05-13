package de.benjaminborbe.selenium.configuration.xml.guice;

import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class SeleniumConfigurationXmlModules implements Modules {

	private final BundleContext context;

	public SeleniumConfigurationXmlModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new SeleniumConfigurationXmlOsgiModule(), new SeleniumConfigurationXmlModule(), new ToolModule());
	}
}
