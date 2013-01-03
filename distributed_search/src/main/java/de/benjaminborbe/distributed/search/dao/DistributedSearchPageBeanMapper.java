package de.benjaminborbe.distributed.search.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.distributed.search.util.MapperDistributedSearchPageIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class DistributedSearchPageBeanMapper extends MapObjectMapperAdapter<DistributedSearchPageBean> {

	public static final String INDEX = "index";

	@Inject
	public DistributedSearchPageBeanMapper(
			final Provider<DistributedSearchPageBean> provider,
			final MapperDistributedSearchPageIdentifier mapperEntryIdentifier,
			final MapperString mapperString,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperEntryIdentifier, mapperString, mapperBoolean, mapperCalendar));
	}

	private static Collection<StringObjectMapper<DistributedSearchPageBean>> buildMappings(final MapperDistributedSearchPageIdentifier mapperEntryIdentifier,
			final MapperString mapperString, final MapperBoolean mapperBoolean, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<DistributedSearchPageBean>> result = new ArrayList<StringObjectMapper<DistributedSearchPageBean>>();
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, DistributedSearchPageIdentifier>("id", mapperEntryIdentifier));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>(INDEX, mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>("title", mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>("content", mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
