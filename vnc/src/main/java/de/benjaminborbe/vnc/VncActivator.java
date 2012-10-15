package de.benjaminborbe.vnc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.guice.VncModules;
import de.benjaminborbe.vnc.xmpp.VncXmppCommandRegistry;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class VncActivator extends BaseBundleActivator {

	@Inject
	private VncService vncService;

	@Inject
	private VncXmppCommandRegistry vncXmppCommandRegistry;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new VncModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(VncService.class, vncService));
		for (final XmppCommand command : vncXmppCommandRegistry.getAll()) {
			result.add(new ServiceInfo(XmppCommand.class, command, command.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new VncServiceTracker(vncRegistry, context,
		// VncService.class));
		return serviceTrackers;
	}

	@Override
	protected void onStopped() {
		super.onStopped();
		try {
			vncService.disconnectForce();
		}
		catch (final Exception e) {
		}
	}

}
