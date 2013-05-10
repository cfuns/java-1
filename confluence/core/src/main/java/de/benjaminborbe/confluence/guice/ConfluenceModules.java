package de.benjaminborbe.confluence.guice;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import de.benjaminborbe.lib.validation.guice.ValidationModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class ConfluenceModules implements Modules {

	private final BundleContext context;

	public ConfluenceModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new ConfluenceOsgiModule(), new ConfluenceModule(), new ToolModule(), new ValidationModule());
	}
}
