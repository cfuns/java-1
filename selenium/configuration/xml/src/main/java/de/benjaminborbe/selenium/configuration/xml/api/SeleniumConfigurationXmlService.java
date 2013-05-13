package de.benjaminborbe.selenium.configuration.xml.api;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public interface SeleniumConfigurationXmlService {

	SeleniumConfigurationIdentifier addXml(String xml) throws SeleniumConfigurationXmlServiceException;

	void deleteXml(SeleniumConfigurationIdentifier seleniumConfiguration) throws SeleniumConfigurationXmlServiceException;
}
