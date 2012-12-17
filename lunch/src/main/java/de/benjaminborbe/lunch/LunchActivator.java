package de.benjaminborbe.lunch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.guice.LunchModules;
import de.benjaminborbe.lunch.service.LunchBookingMessageConsumer;
import de.benjaminborbe.messageservice.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class LunchActivator extends BaseBundleActivator {

	@Inject
	private LunchService lunchService;

	@Inject
	private LunchConfig lunchConfig;

	@Inject
	private LunchBookingMessageConsumer lunchMessageConsumer;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LunchModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(LunchService.class, lunchService));
		for (final ConfigurationDescription configuration : lunchConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		result.add(new ServiceInfo(MessageConsumer.class, lunchMessageConsumer));
		return result;
	}

}
