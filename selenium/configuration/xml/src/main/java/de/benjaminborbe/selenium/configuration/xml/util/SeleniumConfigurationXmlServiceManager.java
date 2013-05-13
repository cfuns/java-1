package de.benjaminborbe.selenium.configuration.xml.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlBean;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlDao;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.tools.util.ParseException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Singleton
public class SeleniumConfigurationXmlServiceManager {

	private final Map<SeleniumConfigurationIdentifier, ServiceRegistration> services = new HashMap<>();

	private final Logger logger;

	private final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao;

	private final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser;

	private BundleContext bundleContext;

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(final BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Inject
	public SeleniumConfigurationXmlServiceManager(
		final Logger logger,
		final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao,
		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser
	) {
		this.logger = logger;
		this.seleniumConfigurationXmlDao = seleniumConfigurationXmlDao;
		this.seleniumGuiConfigurationXmlParser = seleniumGuiConfigurationXmlParser;
	}

	public void onAdded(SeleniumConfigurationIdentifier id) {
		try {
			final SeleniumConfigurationXmlBean seleniumConfigurationXmlBean = seleniumConfigurationXmlDao.load(id);
			final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(seleniumConfigurationXmlBean.getXml());
			bundleContext.registerService(SeleniumConfiguration.class.getName(), seleniumConfiguration, new Properties());
		} catch (ParseException e) {
			logger.warn("add SeleniumConfiguration failed", e);
		}

	}

	public void onRemoved(SeleniumConfigurationIdentifier id) {
		if (services.containsKey(id)) {
			services.get(id).unregister();
		}
	}
}
