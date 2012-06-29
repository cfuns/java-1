package de.benjaminborbe.dhl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.util.Calendar;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.guice.DhlModulesMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ResourceUtil;

public class DhlStatusParserUnitTest {

	@Test
	public void testParse() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		final DhlStatusParser dhlStatusParser = injector.getInstance(DhlStatusParser.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final ResourceUtil resourceUtil = injector.getInstance(ResourceUtil.class);
		final String content = resourceUtil.getResourceContentString("status.html");
		final DhlIdentifier dhlIdentifier = new DhlIdentifier(286476016780l, 65185l);
		final DhlStatus dhlStatus = dhlStatusParser.parseCurrentStatus(dhlIdentifier, content);
		assertNotNull(dhlStatus);
		assertNotNull(dhlStatus.getDhlIdentifier());
		assertEquals(new Long(286476016780l), dhlStatus.getDhlIdentifier().getId());
		assertEquals(new Long(65185l), dhlStatus.getDhlIdentifier().getZip());
		assertEquals("2012-02-29 17:16:00", calendarUtil.toDateTimeString(dhlStatus.getCalendar()));
		assertEquals("--", dhlStatus.getPlace());
		assertEquals("Die Auftragsdaten zu dieser Sendung wurden vom Absender elektronisch an DHL übermittelt.", dhlStatus.getMessage());
	}

	@Test
	public void testParseMessage() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		final DhlStatusParser dhlStatusParser = injector.getInstance(DhlStatusParser.class);
		final StringWriter sw = new StringWriter();
		sw.append("\n");
		sw.append("Die Sendung wurde im Start-Paketzentrum bearbeitet.");
		sw.append("\n");
		assertEquals("Die Sendung wurde im Start-Paketzentrum bearbeitet.", dhlStatusParser.parseMessage(sw.toString()));
	}

	@Test
	public void testParsePlace() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		final DhlStatusParser dhlStatusParser = injector.getInstance(DhlStatusParser.class);
		final StringWriter sw = new StringWriter();
		sw.append("\n");
		sw.append("Bremen");
		sw.append("\n");
		assertEquals("Bremen", dhlStatusParser.parsePlace(sw.toString()));
	}

	@Test
	public void testParseDate() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		final DhlStatusParser dhlStatusParser = injector.getInstance(DhlStatusParser.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final StringWriter sw = new StringWriter();
		sw.append("\n");
		sw.append("Mi, 29.02.12 15:17");
		sw.append("\n");
		sw.append("Uhr");
		sw.append("\n");
		final Calendar calendar = dhlStatusParser.parseDate(sw.toString());
		assertNotNull(calendar);
		assertEquals("2012-02-29 15:17:00", calendarUtil.toDateTimeString(calendar));
	}
}
