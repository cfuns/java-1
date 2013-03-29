package de.benjaminborbe.sample.guice;

import java.util.Arrays;
import java.util.Collection;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;

public class SampleModules implements Modules {

	private final BundleContext context;

	public SampleModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new SampleOsgiModule(), new SampleModule(), new ToolModule());
	}
}
