package de.benjaminborbe.selenium.configuration.xml.guice;

import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;

import java.util.Arrays;
import java.util.Collection;

public class SeleniumConfigurationXmlModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new SeleniumConfigurationXmlOsgiModuleMock(), new SeleniumConfigurationXmlModule(), new ToolModuleMock());
	}
}
