package de.benjaminborbe.poker.player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.util.MapperPokerPlayerIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class PokerPlayerBeanMapper extends MapObjectMapperAdapter<PokerPlayerBean> {

	@Inject
	public PokerPlayerBeanMapper(
			final Provider<PokerPlayerBean> provider,
			final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier,
			final MapperCalendar mapperCalendar,
			final MapperString mapperString,
			final MapperLong mapperLong) {
		super(provider, buildMappings(mapperPokerPlayerIdentifier, mapperCalendar, mapperString, mapperLong));
	}

	private static Collection<StringObjectMapper<PokerPlayerBean>> buildMappings(final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier, final MapperCalendar mapperCalendar,
			final MapperString mapperString, final MapperLong mapperLong) {
		final List<StringObjectMapper<PokerPlayerBean>> result = new ArrayList<StringObjectMapper<PokerPlayerBean>>();
		result.add(new StringObjectMapperAdapter<PokerPlayerBean, PokerPlayerIdentifier>("id", mapperPokerPlayerIdentifier));
		result.add(new StringObjectMapperAdapter<PokerPlayerBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<PokerPlayerBean, Long>("amount", mapperLong));
		result.add(new StringObjectMapperAdapter<PokerPlayerBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<PokerPlayerBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
