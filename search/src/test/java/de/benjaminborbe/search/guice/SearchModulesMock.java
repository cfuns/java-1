package de.benjaminborbe.search.guice;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Module;

import de.benjaminborbe.search.guice.SearchModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import de.benjaminborbe.tools.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.mock.ServletModuleMock;

public class SearchModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new SearchOsgiModuleMock(),
				new SearchModule(), new ToolModule());
	}
}
