package de.benjaminborbe.poker.game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.util.MapperPokerGameIdentifier;
import de.benjaminborbe.poker.util.MapperPokerPlayerIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class PokerGameBeanMapper extends MapObjectMapperAdapter<PokerGameBean> {

	@Inject
	public PokerGameBeanMapper(
			final Provider<PokerGameBean> provider,
			final MapperPokerGameIdentifier mapperPokerGameIdentifier,
			final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier,
			final MapperCalendar mapperCalendar,
			final MapperString mapperString) {
		super(provider, buildMappings(mapperPokerGameIdentifier, mapperPokerPlayerIdentifier, mapperCalendar, mapperString));
	}

	private static Collection<StringObjectMapper<PokerGameBean>> buildMappings(final MapperPokerGameIdentifier mapperPokerGameIdentifier,
			final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier, final MapperCalendar mapperCalendar, final MapperString mapperString) {
		final List<StringObjectMapper<PokerGameBean>> result = new ArrayList<StringObjectMapper<PokerGameBean>>();
		result.add(new StringObjectMapperAdapter<PokerGameBean, PokerGameIdentifier>("id", mapperPokerGameIdentifier));
		result.add(new StringObjectMapperAdapter<PokerGameBean, PokerPlayerIdentifier>("activePlayer", mapperPokerPlayerIdentifier));
		result.add(new StringObjectMapperAdapter<PokerGameBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
