package de.benjaminborbe.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.CacheStorageService;
import de.benjaminborbe.storage.api.PersistentStorageService;
import de.benjaminborbe.storage.guice.StorageModules;
import de.benjaminborbe.storage.servlet.StorageDeleteServlet;
import de.benjaminborbe.storage.servlet.StorageListServlet;
import de.benjaminborbe.storage.servlet.StorageReadServlet;
import de.benjaminborbe.storage.servlet.StorageWriteServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class StorageActivator extends HttpBundleActivator {

	public StorageActivator() {
		super("storage");
	}

	@Inject
	private CacheStorageService cacheStorageService;

	@Inject
	private PersistentStorageService persistentStorageService;

	@Inject
	private StorageReadServlet storageReadServlet;

	@Inject
	private StorageDeleteServlet storageDeleteServlet;

	@Inject
	private StorageListServlet storageListServlet;

	@Inject
	private StorageWriteServlet storageWriteServlet;

	@Override
	protected Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(PersistentStorageService.class, persistentStorageService));
		result.add(new ServiceInfo(CacheStorageService.class, cacheStorageService));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>();
		result.add(new ServletInfo(storageReadServlet, "/read"));
		result.add(new ServletInfo(storageWriteServlet, "/write"));
		result.add(new ServletInfo(storageDeleteServlet, "/delete"));
		result.add(new ServletInfo(storageListServlet, "/list"));
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StorageModules(context);
	}
}
