package de.benjaminborbe.confluence;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.config.ConfluenceConfig;
import de.benjaminborbe.confluence.guice.ConfluenceModules;
import de.benjaminborbe.confluence.search.ConfluenceSearchServiceComponent;
import de.benjaminborbe.confluence.service.ConfluenceRefreshCronJob;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ConfluenceActivator extends BaseBundleActivator {

	@Inject
	private ConfluenceConfig confluenceConfig;

	@Inject
	private ConfluenceService confluenceService;

	@Inject
	private ConfluenceSearchServiceComponent confluenceSearchServiceComponent;

	@Inject
	private ConfluenceRefreshCronJob confluenceRefreshCronJob;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ConfluenceModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ConfluenceService.class, confluenceService));
		result.add(new ServiceInfo(SearchServiceComponent.class, confluenceSearchServiceComponent, confluenceSearchServiceComponent.getClass().getName()));
		result.add(new ServiceInfo(CronJob.class, confluenceRefreshCronJob, confluenceRefreshCronJob.getClass().getName()));
		for (final ConfigurationDescription configuration : confluenceConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

}
