package de.benjaminborbe.storage.memory;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.memory.guice.StorageModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StorageActivator extends HttpBundleActivator {

	public StorageActivator() {
		super(StorageConstants.NAME);
	}

	@Inject
	private StorageService persistentStorageService;

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(StorageService.class, persistentStorageService));
		return result;
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new StorageModules(context);
	}

}
