package de.benjaminborbe.notification;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.config.NotificationConfig;
import de.benjaminborbe.notification.guice.NotificationModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class NotificationActivator extends BaseBundleActivator {

	@Inject
	private NotificationConfig notificationConfig;

	@Inject
	private NotificationService notificationService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NotificationModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NotificationService.class, notificationService));
		for (final ConfigurationDescription configuration : notificationConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
