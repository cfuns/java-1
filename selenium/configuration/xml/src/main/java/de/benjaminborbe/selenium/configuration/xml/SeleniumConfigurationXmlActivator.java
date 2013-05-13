package de.benjaminborbe.selenium.configuration.xml;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlDao;
import de.benjaminborbe.selenium.configuration.xml.guice.SeleniumConfigurationXmlModules;
import de.benjaminborbe.selenium.configuration.xml.service.SeleniumConfigurationXmlServiceImpl;
import de.benjaminborbe.selenium.configuration.xml.util.SeleniumConfigurationXmlServiceManager;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.util.ParseException;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeleniumConfigurationXmlActivator extends BaseBundleActivator {

	@Inject
	private SeleniumConfigurationXmlServiceImpl seleniumConfigurationXmlService;

	@Inject
	private SeleniumConfigurationXmlServiceManager seleniumConfigurationXmlServiceManager;

	@Inject
	private SeleniumConfigurationXmlDao seleniumConfigurationXmlDao;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		seleniumConfigurationXmlServiceManager.setBundleContext(context);
		try {
			final IdentifierIterator<SeleniumConfigurationIdentifier> i = seleniumConfigurationXmlDao.getIdentifierIterator();
			while (i.hasNext()) {
				final SeleniumConfigurationIdentifier id = i.next();
				try {
					seleniumConfigurationXmlServiceManager.onAdded(id);
				} catch (ParseException e) {
					logger.warn("register failed", e);
				}
			}
		} catch (IdentifierIteratorException | StorageException e) {
			logger.warn("register failed", e);
		}
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new SeleniumConfigurationXmlModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(SeleniumConfigurationXmlService.class, seleniumConfigurationXmlService));
		return result;
	}

}
