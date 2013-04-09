package de.benjaminborbe.lunch.wikiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.lunch.util.LunchParseUtil;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.html.HtmlTagParser;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class LunchWikiConnectorUnitTest {

	@Test
	public void testExtractDate() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final LunchParseUtil lunchParseUtil = EasyMock.createNiceMock(LunchParseUtil.class);
		EasyMock.replay(lunchParseUtil);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = null;
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil a = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final LunchWikiConnectorImpl wikiConnector = new LunchWikiConnectorImpl(logger, lunchParseUtil, htmlUtil, a, timeZoneUtil);
		final Calendar calendar = wikiConnector.extractDate("2012-05-03 - Bastians Mittagessen");
		assertNotNull(calendar);
		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(5, calendar.get(Calendar.MONTH) + 1);
		assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH));
	}

}
