package de.benjaminborbe.wow;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.wow.api.WowService;
import de.benjaminborbe.wow.bot.command.WowCommandRegistry;
import de.benjaminborbe.wow.guice.WowModules;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class WowActivator extends BaseBundleActivator {

	@Inject
	private WowService wowService;

	@Inject
	private WowCommandRegistry wowCommandRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new WowModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(WowService.class, wowService));
		for (final XmppCommand command : wowCommandRegistry.getAll()) {
			result.add(new ServiceInfo(XmppCommand.class, command, command.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new WowServiceTracker(wowRegistry, context,
		// WowService.class));
		return serviceTrackers;
	}
}
