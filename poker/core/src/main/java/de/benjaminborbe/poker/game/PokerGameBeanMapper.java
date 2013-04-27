package de.benjaminborbe.poker.game;

import com.google.inject.Provider;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.util.MapperPokerCardIdentifierList;
import de.benjaminborbe.poker.util.MapperPokerGameIdentifier;
import de.benjaminborbe.poker.util.MapperPokerPlayerIdentifierList;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
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
public class PokerGameBeanMapper extends MapObjectMapperAdapter<PokerGameBean> {

	public static final String BOARD_CARDS = "boardCards";

	public static final String BET = "bet";

	public static final String ROUND = "round";

	public static final String BUTTON_POSITION = "buttonPosition";

	public static final String ACTIVE_POSITION = "activePosition";

	public static final String ACTIVE_POSITION_TIME = "activePositionTime";

	public static final String POT = "pot";

	public static final String PLAYERS = "players";

	public static final String CARDS = "cards";

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	public static final String NAME = "name";

	public static final String ID = "id";

	public static final String RUNNING = "running";

	public static final String BIG_BLIND = "bigBlind";

	public static final String SMALL_BLIND = "smallBlind";

	public static final String CARD_POSITION = "cardPosition";

	public static final String ACTIVE_PLAYERS = "activePlayers";

	public static final String MAX_BID = "maxBid";

	public static final String AUTO_FOLD_TIMEOUT = "autoFoldTimeout";

	@Inject
	public PokerGameBeanMapper(
		final Provider<PokerGameBean> provider,
		final MapperPokerGameIdentifier mapperPokerGameIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperString mapperString,
		final MapperBoolean mapperBoolean,
		final MapperLong mapperLong,
		final MapperInteger mapperInteger,
		final MapperPokerCardIdentifierList mapperPokerCardIdentifierList,
		final MapperPokerPlayerIdentifierList mapperPokerPlayerIdentifierList
	) {
		super(provider, buildMappings(mapperPokerGameIdentifier, mapperCalendar, mapperString, mapperBoolean, mapperLong, mapperPokerCardIdentifierList,
			mapperPokerPlayerIdentifierList, mapperInteger));
	}

	private static Collection<StringObjectMapper<PokerGameBean>> buildMappings(
		final MapperPokerGameIdentifier mapperPokerGameIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperString mapperString,
		final MapperBoolean mapperBoolean,
		final MapperLong mapperLong,
		final MapperPokerCardIdentifierList mapperCardIdentifierList,
		final MapperPokerPlayerIdentifierList mapperPlayerIdentifierList,
		final MapperInteger mapperInteger
	) {
		final List<StringObjectMapper<PokerGameBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<PokerGameBean, PokerGameIdentifier>(ID, mapperPokerGameIdentifier));
		result.add(new StringObjectMapperAdapter<PokerGameBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Calendar>(ACTIVE_POSITION_TIME, mapperCalendar));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Boolean>(RUNNING, mapperBoolean));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(AUTO_FOLD_TIMEOUT, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(POT, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(SMALL_BLIND, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(BIG_BLIND, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(MAX_BID, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(ROUND, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Long>(BET, mapperLong));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Integer>(CARD_POSITION, mapperInteger));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Integer>(ACTIVE_POSITION, mapperInteger));
		result.add(new StringObjectMapperAdapter<PokerGameBean, Integer>(BUTTON_POSITION, mapperInteger));
		result.add(new StringObjectMapperAdapter<PokerGameBean, List<PokerCardIdentifier>>(CARDS, mapperCardIdentifierList));
		result.add(new StringObjectMapperAdapter<PokerGameBean, List<PokerCardIdentifier>>(BOARD_CARDS, mapperCardIdentifierList));
		result.add(new StringObjectMapperAdapter<PokerGameBean, List<PokerPlayerIdentifier>>(PLAYERS, mapperPlayerIdentifierList));
		result.add(new StringObjectMapperAdapter<PokerGameBean, List<PokerPlayerIdentifier>>(ACTIVE_PLAYERS, mapperPlayerIdentifierList));
		return result;
	}
}
