package de.benjaminborbe.poker;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.config.PokerConfig;
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

public class PokerActivator extends BaseBundleActivator {

	@Inject
	private PokerCronJob pokerCronJob;

	@Inject
	private PokerConfig pokerConfig;

	@Inject
	private PokerService pokerService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new PokerModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(PokerService.class, pokerService));
		result.add(new ServiceInfo(CronJob.class, pokerCronJob, pokerCronJob.getClass().getName()));
		for (final ConfigurationDescription configuration : pokerConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
