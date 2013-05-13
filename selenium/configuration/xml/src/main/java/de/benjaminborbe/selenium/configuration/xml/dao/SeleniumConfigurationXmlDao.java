package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SeleniumConfigurationXmlDao {

	private final Map<SeleniumConfigurationIdentifier, SeleniumConfigurationXmlBean> data = new HashMap<>();

	@Inject
	public SeleniumConfigurationXmlDao() {
	}

	public SeleniumConfigurationXmlBean create() {
		return new SeleniumConfigurationXmlBean();
	}

	public void save(final SeleniumConfigurationXmlBean bean) {
		data.put(bean.getId(), bean);
	}

	public void delete(final SeleniumConfigurationIdentifier id) {
		data.remove(id);
	}

	public Collection<SeleniumConfigurationIdentifier> list() {
		return data.keySet();
	}
}
