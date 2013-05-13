package de.benjaminborbe.selenium.configuration.xml.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXml;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlService;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXmlServiceException;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlBean;
import de.benjaminborbe.selenium.configuration.xml.dao.SeleniumConfigurationXmlDao;
import de.benjaminborbe.selenium.configuration.xml.util.SeleniumConfigurationXmlServiceManager;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class SeleniumConfigurationXmlServiceImpl implements SeleniumConfigurationXmlService {

	private final Logger logger;

	private final SeleniumConfigurationXmlServiceManager seleniumConfigurationXmlServiceManager;

	private final SeleniumService seleniumService;

	private final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser;

	private final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao;

	@Inject
	public SeleniumConfigurationXmlServiceImpl(
		final Logger logger,
		final SeleniumConfigurationXmlServiceManager seleniumConfigurationXmlServiceManager,
		final SeleniumService seleniumService,
		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser,
		final SeleniumConfigurationXmlDao seleniumConfigurationXmlDao
	) {
		this.logger = logger;
		this.seleniumConfigurationXmlServiceManager = seleniumConfigurationXmlServiceManager;
		this.seleniumService = seleniumService;
		this.seleniumGuiConfigurationXmlParser = seleniumGuiConfigurationXmlParser;
		this.seleniumConfigurationXmlDao = seleniumConfigurationXmlDao;
	}

	@Override
	public Collection<SeleniumConfigurationIdentifier> list(final SessionIdentifier sessionIdentifier) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			return seleniumConfigurationXmlDao.list();
		} catch (StorageException | SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	public SeleniumConfigurationIdentifier addXml(
		final SessionIdentifier sessionIdentifier,
		final String xml
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			logger.debug("addXml");
			final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(xml);
			final SeleniumConfigurationIdentifier id = seleniumConfiguration.getId();
			if (seleniumConfigurationXmlDao.exists(id)) {
				seleniumConfigurationXmlDao.delete(id);
				seleniumConfigurationXmlServiceManager.onRemoved(id);
			}

			final SeleniumConfigurationXmlBean bean = seleniumConfigurationXmlDao.create();
			bean.setId(id);
			bean.setXml(xml);
			seleniumConfigurationXmlDao.save(bean);
			seleniumConfigurationXmlServiceManager.onAdded(id);
			return id;
		} catch (ParseException | StorageException | SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	public void deleteXml(
		final SessionIdentifier sessionIdentifier,
		final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			logger.debug("deleteXml");
			seleniumConfigurationXmlDao.delete(seleniumConfigurationIdentifier);
			seleniumConfigurationXmlServiceManager.onRemoved(seleniumConfigurationIdentifier);
		} catch (StorageException | SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}

	@Override
	public SeleniumConfigurationXml getXml(
		final SessionIdentifier sessionIdentifier, final SeleniumConfigurationIdentifier id
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			seleniumService.expectPermission(sessionIdentifier);
			return seleniumConfigurationXmlDao.load(id);
		} catch (StorageException | SeleniumServiceException e) {
			throw new SeleniumConfigurationXmlServiceException(e);
		}
	}
}
