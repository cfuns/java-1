package de.benjaminborbe.poker;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.api.HandlerRegistration;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.event.PokerPlayerAmountChangedEvent;
import de.benjaminborbe.poker.event.PokerPlayerAmountChangedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerCreatedEvent;
import de.benjaminborbe.poker.event.PokerPlayerCreatedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerDeletedEvent;
import de.benjaminborbe.poker.event.PokerPlayerDeletedEventHandlerImpl;
import de.benjaminborbe.poker.event.PokerPlayerScoreChangedEvent;
import de.benjaminborbe.poker.event.PokerPlayerScoreChangedEventHandlerImpl;
import de.benjaminborbe.poker.guice.PokerModules;
import de.benjaminborbe.poker.service.PokerCronJob;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PokerCoreActivator extends BaseBundleActivator {

	private final List<HandlerRegistration> handlerRegistrations = new ArrayList<HandlerRegistration>();

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
		return result;
	}

	@Override
	protected void onStarted() throws Exception {
		super.onStarted();
		handlerRegistrations.add(eventbusService.addHandler(PokerPlayerCreatedEvent.TYPE, pokerPlayerCreatedEventHandler));
		handlerRegistrations.add(eventbusService.addHandler(PokerPlayerScoreChangedEvent.TYPE, pokerPlayerScoreChangedEventHandler));
		handlerRegistrations.add(eventbusService.addHandler(PokerPlayerDeletedEvent.TYPE, pokerPlayerDeletedEventHandler));
		handlerRegistrations.add(eventbusService.addHandler(PokerPlayerAmountChangedEvent.TYPE, pokerPlayerAmountChangedEventHandler));
	}

	@Override
	protected void onStopped() throws Exception {
		for (final HandlerRegistration handlerRegistration : handlerRegistrations) {
			handlerRegistration.removeHandler();
		}
		super.onStopped();
	}
}
