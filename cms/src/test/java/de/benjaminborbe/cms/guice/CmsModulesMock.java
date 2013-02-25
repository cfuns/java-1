package de.benjaminborbe.cms.guice;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Module;

import de.benjaminborbe.cms.guice.CmsModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.osgi.mock.ServletModuleMock;

public class CmsModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new CmsOsgiModuleMock(), new CmsModule(), new ToolModuleMock());
	}
}
