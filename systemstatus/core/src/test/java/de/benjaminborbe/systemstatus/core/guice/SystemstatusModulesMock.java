package de.benjaminborbe.systemstatus.core.guice;

import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.osgi.mock.ServletModuleMock;

import java.util.Arrays;
import java.util.Collection;

public class SystemstatusModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new SystemstatusOsgiModuleMock(), new SystemstatusModule(), new ToolModuleMock());
	}
}
