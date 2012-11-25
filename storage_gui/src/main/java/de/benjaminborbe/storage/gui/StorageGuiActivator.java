package de.benjaminborbe.storage.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.storage.gui.guice.StorageGuiModules;
import de.benjaminborbe.storage.gui.servlet.StorageBackupServlet;
import de.benjaminborbe.storage.gui.servlet.StorageDeleteServlet;
import de.benjaminborbe.storage.gui.servlet.StorageGuiServlet;
import de.benjaminborbe.storage.gui.servlet.StorageListServlet;
import de.benjaminborbe.storage.gui.servlet.StorageReadServlet;
import de.benjaminborbe.storage.gui.servlet.StorageWriteServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class StorageGuiActivator extends HttpBundleActivator {

	@Inject
	private StorageBackupServlet storageBackupServlet;

	@Inject
	private StorageGuiServlet storageGuiServlet;

	@Inject
	private StorageReadServlet storageReadServlet;

	@Inject
	private StorageDeleteServlet storageDeleteServlet;

	@Inject
	private StorageListServlet storageListServlet;

	@Inject
	private StorageWriteServlet storageWriteServlet;

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
		result.add(new ServletInfo(storageReadServlet, "/read"));
		result.add(new ServletInfo(storageWriteServlet, "/write"));
		result.add(new ServletInfo(storageDeleteServlet, "/delete"));
		result.add(new ServletInfo(storageListServlet, "/list"));
		result.add(new ServletInfo(storageBackupServlet, "/backup"));
		return result;
	}

}
