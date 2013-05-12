package de.benjaminborbe.selenium.configuration.simple.guice;

import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;

import java.util.Arrays;
import java.util.Collection;

public class SeleniumConfigurationSimpleModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new SeleniumConfigurationSimpleOsgiModuleMock(), new SeleniumConfigurationSimpleModule(), new ToolModuleMock());
	}
}
