package de.benjaminborbe.distributed.search.gui.guice;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.guice.ToolModule;
import de.benjaminborbe.website.guice.WebsiteOsgiModule;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import java.util.Arrays;
import java.util.Collection;

public class DistributedSearchGuiModules implements Modules {

	private final BundleContext context;

	public DistributedSearchGuiModules(final BundleContext context) {
		this.context = context;
	}

	@Override
	public Collection<Module> getModules() {
		return Arrays.asList(Peaberry.osgiModule(context), new ServletModule(), new DistributedSearchGuiOsgiModule(), new DistributedSearchGuiModule(), new ToolModule(),
			new WebsiteOsgiModule());
	}
}
