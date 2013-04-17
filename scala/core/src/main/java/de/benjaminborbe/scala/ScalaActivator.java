package de.benjaminborbe.scala;

import com.google.inject.Inject;
import de.benjaminborbe.scala.api.ScalaService;
import de.benjaminborbe.scala.guice.ScalaModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScalaActivator extends BaseBundleActivator {

	@Inject
	private ScalaService scalaService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ScalaModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(ScalaService.class, scalaService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<>(super.getServiceTrackers(context));
		// serviceTrackers.add(new ScalaServiceTracker(scalaRegistry, context,
		// ScalaService.class));
		return serviceTrackers;
	}
}
