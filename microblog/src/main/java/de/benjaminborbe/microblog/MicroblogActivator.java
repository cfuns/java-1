package de.benjaminborbe.microblog;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;
import com.google.inject.Inject;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.guice.MicroblogModules;
import de.benjaminborbe.microblog.service.MicroblogCronJob;
import de.benjaminborbe.microblog.service.MicroblogSearchServiceComponent;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class MicroblogActivator extends BaseBundleActivator {

	@Inject
	private MicroblogCronJob microblogCronJob;

	@Inject
	private MicroblogService microblogService;

	@Inject
	private MicroblogSearchServiceComponent microblogSearchServiceComponent;

	@Inject
	private MicroblogConfig microblogConfig;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MicroblogModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(CronJob.class, microblogCronJob, microblogCronJob.getClass().getName()));
		result.add(new ServiceInfo(MicroblogService.class, microblogService));
		result.add(new ServiceInfo(SearchServiceComponent.class, microblogSearchServiceComponent, microblogSearchServiceComponent.getClass().getName()));
		for (final ConfigurationDescription configuration : microblogConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}
}
