package de.benjaminborbe.proxy.core;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.proxy.core.guice.ProxyCoreModules;
import de.benjaminborbe.proxy.core.util.ProxyServer;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProxyCoreActivator extends BaseBundleActivator {

	@Inject
	private ProxyCoreConfig proxyCoreConfig;

	@Inject
	private ProxyServer proxyServer;

	@Inject
	private ProxyService proxyService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ProxyCoreModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(ProxyService.class, proxyService));
		for (final ConfigurationDescription configuration : proxyCoreConfig.getConfigurations()) {
			result.add(new ServiceInfo(ConfigurationDescription.class, configuration, configuration.getName()));
		}
		return result;
	}

	@Override
	protected void onStarted() {
		try {
			if (proxyCoreConfig.autoStart()) {
				proxyService.start();
			} else {
				logger.trace("skip start proxy, autostart not acitve");
			}
		} catch (ProxyServiceException e) {
			logger.warn("start proxy failed", e);
		}
	}

	@Override
	protected void onStopped() {
		try {
			proxyService.stop();
		} catch (ProxyServiceException e) {
			logger.trace("stop proxy failed", e);
		}
	}
}
