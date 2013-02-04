package de.benjaminborbe.cache.gui.guice;

import java.util.Arrays;
import java.util.Collection;

import com.google.inject.Module;

import de.benjaminborbe.cache.gui.guice.CacheGuiModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModuleMock;
import de.benjaminborbe.tools.osgi.mock.PeaberryModuleMock;
import de.benjaminborbe.tools.osgi.mock.ServletModuleMock;
import de.benjaminborbe.website.guice.WebsiteOsgiModuleMock;

public class CacheGuiModulesMock implements Modules {

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(new PeaberryModuleMock(), new ServletModuleMock(), new CacheGuiOsgiModuleMock(), new CacheGuiModule(), new ToolModuleMock(), new WebsiteOsgiModuleMock());
	}
}
