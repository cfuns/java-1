package de.benjaminborbe.util.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class UtilGuiTimeConvertImplUnitTest {

	@Test
	public void testConvert() throws Exception {

		final UtilGuiTimeConvert utilGuiTimeConvert = new UtilGuiTimeConvertImpl();
		// winterzeit
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			calendar.set(Calendar.YEAR, 2012);
			calendar.set(Calendar.MONTH, 12);
			calendar.set(Calendar.DAY_OF_MONTH, 24);
			calendar.set(Calendar.HOUR_OF_DAY, 10);
			calendar.set(Calendar.MINUTE, 15);
			calendar.set(Calendar.SECOND, 30);
			calendar.set(Calendar.MILLISECOND, 45);
			final Calendar result = utilGuiTimeConvert.convert(calendar, TimeZone.getTimeZone("Europe/Berlin"));
			assertEquals(11, result.get(Calendar.HOUR_OF_DAY));
		}

		// sommerzeit
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			calendar.set(Calendar.YEAR, 2012);
			calendar.set(Calendar.MONTH, 6);
			calendar.set(Calendar.DAY_OF_MONTH, 24);
			calendar.set(Calendar.HOUR_OF_DAY, 10);
			calendar.set(Calendar.MINUTE, 15);
			calendar.set(Calendar.SECOND, 30);
			calendar.set(Calendar.MILLISECOND, 45);
			final Calendar result = utilGuiTimeConvert.convert(calendar, TimeZone.getTimeZone("Europe/Berlin"));
			assertEquals(12, result.get(Calendar.HOUR_OF_DAY));
		}
	}

}
