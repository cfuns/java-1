package de.benjaminborbe.microblog;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.guice.MicroblogModules;
import de.benjaminborbe.microblog.service.MicroblogCronJob;
import de.benjaminborbe.microblog.service.MicroblogSearchServiceComponent;
import de.benjaminborbe.microblog.service.MicroblogServiceTracker;
import de.benjaminborbe.microblog.util.MicroblogPostListenerRegistry;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MicroblogActivator extends BaseBundleActivator {

	@Inject
	private MicroblogPostListenerRegistry microblogPostListenerRegistry;

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

	@Override
	public Collection<ServiceTracker> getServiceTrackers(final BundleContext context) {
		final Set<ServiceTracker> serviceTrackers = new HashSet<ServiceTracker>(super.getServiceTrackers(context));
		serviceTrackers.add(new MicroblogServiceTracker(microblogPostListenerRegistry, context, MicroblogPostListener.class));
		return serviceTrackers;
	}
}
