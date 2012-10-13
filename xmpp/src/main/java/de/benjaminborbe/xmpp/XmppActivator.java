package de.benjaminborbe.xmpp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Inject;

import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.guice.XmppModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class XmppActivator extends BaseBundleActivator {

	@Inject
	private XmppService xmppService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new XmppModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(XmppService.class, xmppService));
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new XmppServiceTracker(xmppRegistry, context,
		// XmppService.class));
		return serviceTrackers;
	}
}
