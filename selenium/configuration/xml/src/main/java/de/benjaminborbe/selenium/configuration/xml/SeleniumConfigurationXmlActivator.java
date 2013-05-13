package de.benjaminborbe.selenium.configuration.xml;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.configuration.xml.guice.SeleniumConfigurationXmlModules;
import de.benjaminborbe.selenium.configuration.xml.service.SeleniumConfigurationXml;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumConfigurationXmlActivator extends BaseBundleActivator {

	@Inject
	private SeleniumConfigurationXml seleniumConfigurationXml;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumConfigurationXmlModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SeleniumConfiguration.class, seleniumConfigurationXml));
		return result;
	}
}
