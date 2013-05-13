package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import java.util.Collection;

public interface SeleniumConfigurationXmlDao {

	SeleniumConfigurationXmlBean create();

	void save(final SeleniumConfigurationXmlBean bean);

	void delete(final SeleniumConfigurationIdentifier id);

	Collection<SeleniumConfigurationIdentifier> list();

	SeleniumConfigurationXmlBean load(final SeleniumConfigurationIdentifier id);

	boolean exists(final SeleniumConfigurationIdentifier id);
}