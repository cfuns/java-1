package de.benjaminborbe.storage.tools;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperBase;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

public class TestBeanMapper extends MapObjectMapperBase<TestBean> {

	@Inject
	public TestBeanMapper(final Provider<TestBean> provider) {
		super(provider);
	}

	@Override
	public void map(final TestBean object, final Map<String, String> data) throws MapException {
		data.put("id", object.getId() != null ? object.getId().getId() : null);
		data.put("name", object.getName());
	}

	@Override
	public void map(final Map<String, String> data, final TestBean object) throws MapException {
		object.setId(data.get("id") != null ? new TestIdentifier(data.get("id")) : null);
		object.setName(data.get("name"));
	}

	@Override
	public void map(final TestBean object, final Map<String, String> data, final Collection<String> fieldNames) throws MapException {
	}

	@Override
	public void map(final Map<String, String> data, final TestBean object, final Collection<String> fieldNames) throws MapException {
	}
}
