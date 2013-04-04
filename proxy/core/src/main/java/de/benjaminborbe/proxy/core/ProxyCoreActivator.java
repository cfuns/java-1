package de.benjaminborbe.proxy.core;

import com.google.inject.Inject;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.guice.ProxyCoreModules;
import de.benjaminborbe.proxy.core.util.ProxySocket;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProxyCoreActivator extends BaseBundleActivator {

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
