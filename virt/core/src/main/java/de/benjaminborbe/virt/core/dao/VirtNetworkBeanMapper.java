package de.benjaminborbe.virt.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.core.util.MapperVirtNetworkIdentifier;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class VirtNetworkBeanMapper extends MapObjectMapperAdapter<VirtNetworkBean> {

	@Inject
	public VirtNetworkBeanMapper(
		final Provider<VirtNetworkBean> provider,
		final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperVirtNetworkIdentifier mapperVirtNetworkIdentifier
	) {
		super(provider, buildMappings(mapperString, mapperCalendar, mapperVirtNetworkIdentifier));
	}

	private static Collection<StringObjectMapper<VirtNetworkBean>> buildMappings(final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperVirtNetworkIdentifier mapperVirtNetworkIdentifier) {
		final List<StringObjectMapper<VirtNetworkBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, VirtNetworkIdentifier>("id", mapperVirtNetworkIdentifier));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
