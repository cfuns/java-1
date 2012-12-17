package de.benjaminborbe.configuration.util;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperBase;

public class ConfigurationBeanMapper extends MapObjectMapperBase<ConfigurationBean> {

	private static final String ID = "id";

	private static final String VALUE = "value";

	@Inject
	public ConfigurationBeanMapper(final Provider<ConfigurationBean> provider) {
		super(provider);
	}

	@Override
	public void map(final ConfigurationBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(VALUE, object.getValue());
	}

	@Override
	public void map(final Map<String, String> data, final ConfigurationBean object) throws MapException {
		object.setId(toConfigurationIdentifier(data.get(ID)));
		object.setValue(data.get(VALUE));
	}

	private ConfigurationIdentifier toConfigurationIdentifier(final String id) {
		return id != null ? new ConfigurationIdentifier(id) : null;
	}

	private String toString(final ConfigurationIdentifier id) {
		return id != null ? id.getId() : null;
	}

}
