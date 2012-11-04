package de.benjaminborbe.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.guice.StorageModules;
import de.benjaminborbe.storage.util.StorageConnectionPool;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class StorageActivator extends HttpBundleActivator {

	public StorageActivator() {
		super("storage");
	}

	//
	// @Inject
	// private StorageConfig storageConfig;

	@Inject
	private StorageService persistentStorageService;

	@Inject
	private StorageConnectionPool storageConnectionPool;

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(StorageService.class, persistentStorageService));
		// for (final ConfigurationDescription configuration :
		// storageConfig.getConfigurations()) {
		// result.add(new ServiceInfo(ConfigurationDescription.class, configuration,
		// configuration.getName()));
		// }
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StorageModules(context);
	}

	@Override
	protected void onStopped() {
		super.onStopped();
		storageConnectionPool.close();
	}
}
