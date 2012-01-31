package de.benjaminborbe.storage.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.storage.gui.guice.StorageGuiModules;
import de.benjaminborbe.storage.gui.servlet.StorageGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class StorageGuiActivator extends HttpBundleActivator {

	@Inject
	private StorageGuiServlet storageGuiServlet;

	public StorageGuiActivator() {
		super("storage");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StorageGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(storageGuiServlet, "/"));
		return result;
	}

}
