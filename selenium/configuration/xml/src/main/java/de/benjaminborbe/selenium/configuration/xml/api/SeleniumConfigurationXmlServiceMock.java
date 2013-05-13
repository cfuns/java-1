package de.benjaminborbe.selenium.configuration.xml.api;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public class SeleniumConfigurationXmlServiceMock implements SeleniumConfigurationXmlService {

	@Override
	public SeleniumConfigurationIdentifier addXml(final String xml) throws SeleniumConfigurationXmlServiceException {
		return null;
	}

	@Override
	public void deleteXml(final SeleniumConfigurationIdentifier seleniumConfiguration) throws SeleniumConfigurationXmlServiceException {
	}
}
