package de.benjaminborbe.distributed.search.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.distributed.search.util.MapperDistributedSearchPageIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class DistributedSearchPageBeanMapper extends MapObjectMapperAdapter<DistributedSearchPageBean> {

	public static final String DATE = "date";

	public static final String ID = "id";

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	public static final String CONTENT = "content";

	public static final String TITLE = "title";

	public static final String INDEX = "index";

	@Inject
	public DistributedSearchPageBeanMapper(
		final Provider<DistributedSearchPageBean> provider,
		final MapperDistributedSearchPageIdentifier mapperEntryIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperEntryIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<DistributedSearchPageBean>> buildMappings(final MapperDistributedSearchPageIdentifier mapperEntryIdentifier,
																																												 final MapperString mapperString, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<DistributedSearchPageBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, DistributedSearchPageIdentifier>(ID, mapperEntryIdentifier));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>(INDEX, mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>(TITLE, mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, String>(CONTENT, mapperString));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, Calendar>(DATE, mapperCalendar));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<DistributedSearchPageBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
