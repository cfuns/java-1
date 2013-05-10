package de.benjaminborbe.wiki.guice;

import com.google.inject.Module;
import de.benjaminborbe.lib.validation.guice.ValidationModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.osgi.mock.ServletModuleMock;

import java.util.Arrays;
import java.util.Collection;

public class WikiModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new WikiOsgiModuleMock(), new WikiModule(), new ToolModuleMock(), new ValidationModule());
	}
}
