package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public class SeleniumConfigurationXmlBean {

	private SeleniumConfigurationIdentifier id;

	private String xml;

	public SeleniumConfigurationIdentifier getId() {
		return id;
	}

	public void setId(final SeleniumConfigurationIdentifier id) {
		this.id = id;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(final String xml) {
		this.xml = xml;
	}
}
