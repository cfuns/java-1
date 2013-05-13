package de.benjaminborbe.selenium.configuration.xml.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlServiceException;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlBean;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlDao;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class SeleniumConfigurationXmlServiceImpl implements SeleniumConfigurationXmlService {

	private final Logger logger;

	private final SeleniumService seleniumService;

	private final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser;

	private final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao;

	@Inject
	public SeleniumConfigurationXmlServiceImpl(
		Logger logger,
		final SeleniumService seleniumService,
		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser,
		final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao
	) {
		this.logger = logger;
		this.seleniumService = seleniumService;
		this.seleniumGuiConfigurationXmlParser = seleniumGuiConfigurationXmlParser;
		this.seleniumConfigurationXmlDao = seleniumConfigurationXmlDao;
	}

	@Override
	public Collection<SeleniumConfigurationIdentifier> list(final SessionIdentifier sessionIdentifier) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			return seleniumConfigurationXmlDao.list();
		} catch (SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	public SeleniumConfigurationIdentifier addXml(
		SessionIdentifier sessionIdentifier,
		String xml
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			logger.debug("addXml");
			final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(xml);
			SeleniumConfigurationXmlBean bean = seleniumConfigurationXmlDao.create();
			bean.setId(seleniumConfiguration.getId());
			bean.setXml(xml);
			seleniumConfigurationXmlDao.save(bean);
			return seleniumConfiguration.getId();
		} catch (ParseException | SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	public void deleteXml(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier seleniumConfiguration
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			logger.debug("deleteXml");
			seleniumConfigurationXmlDao.delete(seleniumConfiguration);
		} catch (SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}
}
