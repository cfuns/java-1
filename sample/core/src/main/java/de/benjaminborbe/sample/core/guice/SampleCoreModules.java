package de.benjaminborbe.sample.core.guice;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class SampleCoreModules implements Modules {

	private final BundleContext context;

	public SampleCoreModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new SampleCoreOsgiModule(), new SampleCoreModule(), new ToolModule());
	}
}
