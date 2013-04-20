package de.benjaminborbe.imagedownloader.core;

import javax.inject.Inject;
import de.benjaminborbe.imagedownloader.api.ImagedownloaderService;
import de.benjaminborbe.imagedownloader.core.guice.ImagedownloaderCoreModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ImagedownloaderCoreActivator extends BaseBundleActivator {

	@Inject
	private ImagedownloaderService imagedownloaderService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ImagedownloaderCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(ImagedownloaderService.class, imagedownloaderService));
		return result;
	}

}
