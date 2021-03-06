package de.benjaminborbe.lunch.wikiconnector;

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
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LunchWikiConnectorImplUnitTest {

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
		assertThat(calendar, is(notNullValue()));
		assertThat(2012, is(calendar.get(Calendar.YEAR)));
		assertThat(5, is(calendar.get(Calendar.MONTH) + 1));
		assertThat(3, is(calendar.get(Calendar.DAY_OF_MONTH)));
	}

	@Test
	public void testIsLunchTitle() throws Exception {
		final LunchWikiConnectorImpl lunchWikiConnector = new LunchWikiConnectorImpl(null, null, null, null, null);
		assertThat(lunchWikiConnector.isLunchTitle("Foo"), is(false));
		assertThat(lunchWikiConnector.isLunchTitle("2012-05-03 - Bastians Mittagessen"), is(true));
		assertThat(lunchWikiConnector.isLunchTitle("2014-01-10 Bastians Mittagessen — Name des Essens"), is(true));
		assertThat(lunchWikiConnector.isLunchTitle("2014-01-10 Name des Essen"), is(true));
		assertThat(lunchWikiConnector.isLunchTitle("2014-01-10 - Name des Essen"), is(true));
	}
}
