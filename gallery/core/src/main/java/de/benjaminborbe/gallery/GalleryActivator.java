package de.benjaminborbe.gallery;

import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.guice.GalleryModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GalleryActivator extends BaseBundleActivator {

	@Inject
	private GalleryService galleryService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new GalleryModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(GalleryService.class, galleryService));
		return result;
	}
}
