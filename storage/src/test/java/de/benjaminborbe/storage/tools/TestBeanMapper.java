package de.benjaminborbe.storage.tools;

import java.util.Map;

import com.google.inject.Provider;

import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

public class TestBeanMapper extends BaseMapper<TestBean> {

	public TestBeanMapper(final Provider<TestBean> provider) {
		super(provider);
	}

	@Override
	public void map(final TestBean object, final Map<String, String> data) throws MapException {
		data.put("id", object.getId());
		data.put("name", object.getName());
	}

	@Override
	public void map(final Map<String, String> data, final TestBean object) throws MapException {
		object.setId(data.get("id"));
		object.setName(data.get("name"));
	}
}
