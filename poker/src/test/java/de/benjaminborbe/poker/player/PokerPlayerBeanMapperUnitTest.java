package de.benjaminborbe.poker.player;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.poker.card.PokerCardIdentifierBuilder;
import de.benjaminborbe.poker.util.MapperPokerCardIdentifier;
import de.benjaminborbe.poker.util.MapperPokerCardIdentifierList;
import de.benjaminborbe.poker.util.MapperPokerGameIdentifier;
import de.benjaminborbe.poker.util.MapperPokerPlayerIdentifier;
import de.benjaminborbe.poker.util.MapperUserIdentifier;
import de.benjaminborbe.poker.util.MapperUserIdentifierCollection;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

@RunWith(Parameterized.class)
public class PokerPlayerBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public PokerPlayerBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<Object[]>();
		result.add(new Object[] { "id", "1337" });
		result.add(new Object[] { "name", "bla" });
		result.add(new Object[] { "created", "123456" });
		result.add(new Object[] { "modified", "123456" });
		result.add(new Object[] { "amount", "1337" });
		result.add(new Object[] { "game", "1337" });
		result.add(new Object[] { "bet", "1337" });
		result.add(new Object[] { "token", "asdfasdf" });
		result.add(new Object[] { "owners", "bgates" });
		return result;
	}

	private PokerPlayerBeanMapper getPokerPlayerBeanMapper() {
		final Provider<PokerPlayerBean> beanProvider = new ProviderMock<PokerPlayerBean>(PokerPlayerBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier = new MapperPokerPlayerIdentifier();
		final MapperLong mapperLong = new MapperLong(parseUtil);
		final MapperPokerGameIdentifier mapperPokerGameIdentifier = new MapperPokerGameIdentifier();
		final PokerCardIdentifierBuilder pokerCardIdentifierBuilder = new PokerCardIdentifierBuilder(logger, parseUtil);
		final MapperPokerCardIdentifier mapperPokerCardIdentifier = new MapperPokerCardIdentifier(pokerCardIdentifierBuilder);
		final MapperPokerCardIdentifierList mapperPokerCardIdentifierList = new MapperPokerCardIdentifierList(mapperPokerCardIdentifier);
		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperUserIdentifierCollection mapperUserIdentifierCollection = new MapperUserIdentifierCollection(mapperUserIdentifier);
		return new PokerPlayerBeanMapper(beanProvider, mapperUserIdentifierCollection, mapperPokerPlayerIdentifier, mapperPokerGameIdentifier, mapperCalendar, mapperString,
				mapperLong, mapperPokerCardIdentifierList);
	}

	@Test
	public void testMaxRetryCounter() throws Exception {
		final PokerPlayerBeanMapper mapper = getPokerPlayerBeanMapper();
		final Map<String, String> inputData = new HashMap<String, String>();
		inputData.put(fieldName, fieldValue);
		final PokerPlayerBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
