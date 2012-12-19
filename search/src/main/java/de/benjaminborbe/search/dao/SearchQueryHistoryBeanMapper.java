package de.benjaminborbe.search.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBase;

@Singleton
public class SearchQueryHistoryBeanMapper extends MapObjectMapperAdapter<SearchQueryHistoryBean> {

	public static final String OWNER = "owner";

	@Inject
	public SearchQueryHistoryBeanMapper(
			final Provider<SearchQueryHistoryBean> provider,
			final SearchQueryHistoryIdentifierMapper mapperListIdentifier,
			final MapperString mapperString,
			final MapperCalendar mapperCalendar

	) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<SearchQueryHistoryBean>> buildMappings(final SearchQueryHistoryIdentifierMapper mapperListIdentifier,
			final MapperString mapperString, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<SearchQueryHistoryBean>> result = new ArrayList<StringObjectMapper<SearchQueryHistoryBean>>();
		result.add(new StringObjectMapperBase<SearchQueryHistoryBean, SearchQueryHistoryIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperBase<SearchQueryHistoryBean, String>("query", mapperString));
		result.add(new StringObjectMapperBase<SearchQueryHistoryBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperBase<SearchQueryHistoryBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
