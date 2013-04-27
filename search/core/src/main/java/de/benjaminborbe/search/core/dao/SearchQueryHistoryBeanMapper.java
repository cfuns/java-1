package de.benjaminborbe.search.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class SearchQueryHistoryBeanMapper extends MapObjectMapperAdapter<SearchQueryHistoryBean> {

	public static final String OWNER = "owner";

	@Inject
	public SearchQueryHistoryBeanMapper(
		final Provider<SearchQueryHistoryBean> provider,
		final MapperSearchQueryHistoryIdentifier mapperListIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperUserIdentifier mapperUserIdentifier
	) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar, mapperUserIdentifier));
	}

	private static Collection<StringObjectMapper<SearchQueryHistoryBean>> buildMappings(
		final MapperSearchQueryHistoryIdentifier mapperListIdentifier,
		final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperUserIdentifier mapperUserIdentifier
	) {
		final List<StringObjectMapper<SearchQueryHistoryBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<SearchQueryHistoryBean, SearchQueryHistoryIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<SearchQueryHistoryBean, UserIdentifier>("user", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<SearchQueryHistoryBean, String>("query", mapperString));
		result.add(new StringObjectMapperAdapter<SearchQueryHistoryBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<SearchQueryHistoryBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
