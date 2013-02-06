package de.benjaminborbe.poker;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.guice.PokerModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class PokerActivator extends BaseBundleActivator {

	@Inject
	private PokerService pokerService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PokerModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(PokerService.class, pokerService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new PokerServiceTracker(pokerRegistry, context,
		// PokerService.class));
		return serviceTrackers;
	}
}
