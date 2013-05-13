package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

import java.util.Collection;

public interface SeleniumConfigurationXmlDao extends Dao<SeleniumConfigurationXmlBean, SeleniumConfigurationIdentifier> {

	SeleniumConfigurationXmlBean create();

	void save(final SeleniumConfigurationXmlBean bean) throws StorageException;

	void delete(final SeleniumConfigurationIdentifier id) throws StorageException;

	Collection<SeleniumConfigurationIdentifier> list() throws StorageException;

	SeleniumConfigurationXmlBean load(final SeleniumConfigurationIdentifier id) throws StorageException;

	boolean exists(final SeleniumConfigurationIdentifier id) throws StorageException;
}