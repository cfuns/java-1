package de.benjaminborbe.virt.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.virt.api.VirtIpAddress;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.core.util.MapperVirtIpAddress;
import de.benjaminborbe.virt.core.util.MapperVirtNetworkIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class VirtNetworkBeanMapper extends MapObjectMapperAdapter<VirtNetworkBean> {

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String IP = "ip";

	@Inject
	public VirtNetworkBeanMapper(
		final Provider<VirtNetworkBean> provider,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperVirtNetworkIdentifier mapperVirtNetworkIdentifier,
		final MapperVirtIpAddress mapperVirtIpAddress
	) {
		super(provider, buildMappings(mapperString, mapperCalendar, mapperVirtNetworkIdentifier, mapperVirtIpAddress));
	}

	private static Collection<StringObjectMapper<VirtNetworkBean>> buildMappings(
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperVirtNetworkIdentifier mapperVirtNetworkIdentifier,
		final MapperVirtIpAddress mapperVirtIpAddress
	) {
		final List<StringObjectMapper<VirtNetworkBean>> result = new ArrayList<StringObjectMapper<VirtNetworkBean>>();
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, VirtNetworkIdentifier>(ID, mapperVirtNetworkIdentifier));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<VirtNetworkBean, VirtIpAddress>(IP, mapperVirtIpAddress));
		return result;
	}
}
