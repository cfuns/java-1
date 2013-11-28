package de.benjaminborbe.scala.core;

import de.benjaminborbe.scala.api.ScalaService;
import de.benjaminborbe.scala.core.guice.ScalaModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ScalaActivator extends BaseBundleActivator {

	@Inject
	private ScalaService scalaService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ScalaModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(ScalaService.class, scalaService));
		return result;
	}

}
