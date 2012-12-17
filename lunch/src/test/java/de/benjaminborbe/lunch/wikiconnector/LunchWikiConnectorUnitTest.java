package de.benjaminborbe.lunch.wikiconnector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.lunch.util.LunchParseUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.DateUtilImpl;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;

public class LunchWikiConnectorUnitTest {

	@Test
	public void testExtractDate() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final LunchParseUtil lunchParseUtil = EasyMock.createNiceMock(LunchParseUtil.class);
		EasyMock.replay(lunchParseUtil);

		final DateUtil dateUtil = new DateUtilImpl();
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		final LunchWikiConnectorImpl wikiConnector = new LunchWikiConnectorImpl(logger, dateUtil, lunchParseUtil, htmlUtil);
		final Date date = wikiConnector.extractDate("2012-05-03 - Bastians Mittagessen");
		assertNotNull(date);
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(5, calendar.get(Calendar.MONTH) + 1);
		assertEquals(3, calendar.get(Calendar.DAY_OF_MONTH));
	}

}
