package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.configuration.xml.api.SeleniumConfigurationXml;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class SeleniumConfigurationXmlBean implements Entity<SeleniumConfigurationIdentifier>, HasCreated, HasModified, SeleniumConfigurationXml {

	private SeleniumConfigurationIdentifier id;

	private String xml;

	private Calendar created;

	private Calendar modified;

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

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
