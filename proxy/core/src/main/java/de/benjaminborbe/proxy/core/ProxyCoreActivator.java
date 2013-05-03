package de.benjaminborbe.proxy.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.osgi.framework.BundleContext;

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.proxy.core.guice.ProxyCoreModules;
import de.benjaminborbe.proxy.core.util.ProxySocket;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class ProxyCoreActivator extends BaseBundleActivator {

    @Inject
    private ProxyCoreConfig proxyCoreConfig;

    @Inject
    private ProxySocket proxySocket;

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
    protected void onStopped() {
        try {
            proxyService.stop();
        } catch (ProxyServiceException e) {
            // nop
        }
    }
}
