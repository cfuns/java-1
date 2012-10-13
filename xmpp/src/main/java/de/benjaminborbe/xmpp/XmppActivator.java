package de.benjaminborbe.xmpp;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.config.XmppConfig;
import de.benjaminborbe.xmpp.connector.XmppConnector;
import de.benjaminborbe.xmpp.connector.XmppConnectorException;
import de.benjaminborbe.xmpp.guice.XmppModules;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class XmppActivator extends BaseBundleActivator {

	@Inject
	private XmppConnector xmppConnector;

	@Inject
	private XmppService xmppService;

	@Inject
	private XmppConfig xmppConfig;

	@Inject
	private Logger logger;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new XmppModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(XmppService.class, xmppService));
		for (final ConfigurationDescription configuration : xmppConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		// serviceTrackers.add(new XmppServiceTracker(xmppRegistry, context,
		// XmppService.class));
		return serviceTrackers;
	}

	@Override
	protected void onStarted() {
		super.onStarted();

		try {
			xmppConnector.connect();
		}
		catch (final XmppConnectorException e) {
			logger.debug(e.getClass().getName(), e);
		}

	}

	@Override
	protected void onStopped() {
		super.onStopped();

		try {
			xmppConnector.disconnect();
		}
		catch (final XmppConnectorException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

}
