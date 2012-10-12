package de.benjaminborbe.dhl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.util.Calendar;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.dhl.guice.DhlModulesMock;
import de.benjaminborbe.dhl.status.DhlBean;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ResourceUtil;

public class DhlStatusParserIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		assertNotNull(injector.getInstance(DhlStatusParser.class));
	}

	@Test
	public void testParse() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		final DhlStatusParser dhlStatusParser = injector.getInstance(DhlStatusParser.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);
		final ResourceUtil resourceUtil = injector.getInstance(ResourceUtil.class);
		final String content = resourceUtil.getResourceContentAsString("status.html");
		final DhlBean dhl = new DhlBean();
		dhl.setId(new DhlIdentifier("286476016780"));
		dhl.setTrackingNumber(286476016780l);
		dhl.setZip(65185l);
		final DhlStatus dhlStatus = dhlStatusParser.parseCurrentStatus(dhl, content);
		assertNotNull(dhlStatus);
		assertEquals(286476016780l, dhlStatus.getDhl().getTrackingNumber());
		assertEquals(65185l, dhlStatus.getDhl().getZip());
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
