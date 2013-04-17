package de.benjaminborbe.kiosk;

import com.google.inject.Inject;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.config.KioskConfig;
import de.benjaminborbe.kiosk.guice.KioskModules;
import de.benjaminborbe.kiosk.service.KioskBookingMessageConsumer;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KioskActivator extends BaseBundleActivator {

	@Inject
	private KioskService kioskService;

	@Inject
	private KioskBookingMessageConsumer kioskBookingMessageConsumer;

	@Inject
	private KioskConfig kioskConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new KioskModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(KioskService.class, kioskService));
		result.add(new ServiceInfo(MessageConsumer.class, kioskBookingMessageConsumer));
		for (final ConfigurationDescription configuration : kioskConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
