package de.benjaminborbe.xmlrpc;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.xmlrpc.api.XmlrpcService;
import de.benjaminborbe.xmlrpc.guice.XmlrpcModules;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class XmlrpcActivator extends BaseBundleActivator {

	@Inject
	private XmlrpcService xmlrpcService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new XmlrpcModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(XmlrpcService.class, xmlrpcService));
		return result;
	}

}
