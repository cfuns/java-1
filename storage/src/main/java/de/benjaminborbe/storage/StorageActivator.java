package de.benjaminborbe.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.Configuration;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.guice.StorageModules;
import de.benjaminborbe.storage.util.StorageConfig;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class StorageActivator extends HttpBundleActivator {

	public StorageActivator() {
		super("storage");
	}

	@Inject
	private StorageConfig storageConfig;

	@Inject
	private StorageService persistentStorageService;

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(StorageService.class, persistentStorageService));
		for (final Configuration<?> configuration : storageConfig.getConfigurations()) {
			result.add(new ServiceInfo(Configuration.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StorageModules(context);
	}
}
