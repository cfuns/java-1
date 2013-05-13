package de.benjaminborbe.selenium.configuration.xml.service;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlBean;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlDao;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SeleniumConfigurationXmlService {

	private final Logger logger;

	private final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser;

	private final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao;

	@Inject
	public SeleniumConfigurationXmlService(
		Logger logger,
		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser,
		final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao
	) {
		this.logger = logger;
		this.seleniumGuiConfigurationXmlParser = seleniumGuiConfigurationXmlParser;
		this.seleniumConfigurationXmlDao = seleniumConfigurationXmlDao;
	}

	public SeleniumConfigurationIdentifier addXml(String xml) throws SeleniumConfigurationXmlServiceException {
		try {
			logger.debug("addXml");
			final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(xml);
			SeleniumConfigurationXmlBean bean = seleniumConfigurationXmlDao.create();
			bean.setId(seleniumConfiguration.getId());
			bean.setXml(xml);
			seleniumConfigurationXmlDao.save(bean);
			return seleniumConfiguration.getId();
		} catch (ParseException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	public void deleteXml(SeleniumConfigurationIdentifier seleniumConfiguration) throws SeleniumConfigurationXmlServiceException {
		logger.debug("deleteXml");
		seleniumConfigurationXmlDao.delete(seleniumConfiguration);
	}
}
