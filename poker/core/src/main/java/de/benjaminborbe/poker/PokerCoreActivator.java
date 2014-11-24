package de.benjaminborbe.poker;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.event.PokerPlayerAmountChangedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerCreatedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerDeletedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerScoreChangedEventHandlerImpl;
import de.benjaminborbe.poker.guice.PokerModules;
import de.benjaminborbe.poker.service.PokerCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PokerCoreActivator extends BaseBundleActivator {

	@Inject
	private PokerCronJob pokerCronJob;

	@Inject
	private PokerCoreConfig pokerCoreConfig;

	@Inject
	private PokerService pokerService;

	@Inject
	private EventbusService eventbusService;

	@Inject
	private PokerPlayerCreatedEventHandlerImpl pokerPlayerCreatedEventHandler;

	@Inject
	private PokerPlayerScoreChangedEventHandlerImpl pokerPlayerScoreChangedEventHandler;

	@Inject
	private PokerPlayerDeletedEventHandlerImpl pokerPlayerDeletedEventHandler;

	@Inject
	private PokerPlayerAmountChangedEventHandlerImpl pokerPlayerAmountChangedEventHandler;

	@Inject
	private AuthenticationService authenticationService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PokerModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(PokerService.class, pokerService));
		result.add(new ServiceInfo(CronJob.class, pokerCronJob, pokerCronJob.getClass().getName()));
		for (final ConfigurationDescription configuration : pokerCoreConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		result.add(new ServiceInfo(EventHandler.class, pokerPlayerCreatedEventHandler, pokerPlayerCreatedEventHandler.getClass().getName()));
		result.add(new ServiceInfo(EventHandler.class, pokerPlayerScoreChangedEventHandler, pokerPlayerScoreChangedEventHandler.getClass().getName()));
		result.add(new ServiceInfo(EventHandler.class, pokerPlayerDeletedEventHandler, pokerPlayerDeletedEventHandler.getClass().getName()));
		result.add(new ServiceInfo(EventHandler.class, pokerPlayerAmountChangedEventHandler, pokerPlayerAmountChangedEventHandler.getClass().getName()));
		return result;
	}

}
