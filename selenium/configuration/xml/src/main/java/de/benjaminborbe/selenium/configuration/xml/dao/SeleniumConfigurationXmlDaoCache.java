package de.benjaminborbe.selenium.configuration.xml.dao;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SeleniumConfigurationXmlDaoCache implements SeleniumConfigurationXmlDao {

	private final Map<SeleniumConfigurationIdentifier, SeleniumConfigurationXmlBean> data = new HashMap<>();

	@Inject
	public SeleniumConfigurationXmlDaoCache() {
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

	public SeleniumConfigurationXmlBean load(final SeleniumConfigurationIdentifier id) {
		return data.get(id);
	}

	public boolean exists(final SeleniumConfigurationIdentifier id) {
		return data.containsKey(id);
	}
}
