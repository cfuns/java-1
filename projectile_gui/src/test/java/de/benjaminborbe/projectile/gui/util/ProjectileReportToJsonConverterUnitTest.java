package de.benjaminborbe.projectile.gui.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.easymock.EasyMock;
import org.json.simple.JSONObject;
import org.junit.Test;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;

public class ProjectileReportToJsonConverterUnitTest {

	@Test
	public void testConvert() throws Exception {
		final ProjectileReportToJsonConverter converter = new ProjectileReportToJsonConverter();

		{
			final ProjectileSlacktimeReport report = EasyMock.createMock(ProjectileSlacktimeReport.class);
			EasyMock.expect(report.getName()).andReturn("foobar").anyTimes();
			EasyMock.expect(report.getWeekExtern()).andReturn(null).anyTimes();
			EasyMock.expect(report.getWeekIntern()).andReturn(null).anyTimes();
			EasyMock.expect(report.getMonthExtern()).andReturn(null).anyTimes();
			EasyMock.expect(report.getMonthIntern()).andReturn(null).anyTimes();
			EasyMock.expect(report.getYearExtern()).andReturn(null).anyTimes();
			EasyMock.expect(report.getYearIntern()).andReturn(null).anyTimes();
			EasyMock.replay(report);

			assertEquals("{\"username\":\"foobar\",\"month\":{\"extern\":null,\"intern\":null},\"year\":{\"extern\":null,\"intern\":null},\"week\":{\"extern\":null,\"intern\":null}}",
					toString(converter, report));
		}

		{
			final ProjectileSlacktimeReport report = EasyMock.createMock(ProjectileSlacktimeReport.class);
			EasyMock.expect(report.getName()).andReturn("foobar").anyTimes();
			EasyMock.expect(report.getWeekExtern()).andReturn(1.1).anyTimes();
			EasyMock.expect(report.getWeekIntern()).andReturn(2.2).anyTimes();
			EasyMock.expect(report.getMonthExtern()).andReturn(3.3).anyTimes();
			EasyMock.expect(report.getMonthIntern()).andReturn(4.4).anyTimes();
			EasyMock.expect(report.getYearExtern()).andReturn(5.5).anyTimes();
			EasyMock.expect(report.getYearIntern()).andReturn(6.6).anyTimes();
			EasyMock.replay(report);

			assertEquals("{\"username\":\"foobar\",\"month\":{\"extern\":3.3,\"intern\":4.4},\"year\":{\"extern\":5.5,\"intern\":6.6},\"week\":{\"extern\":1.1,\"intern\":2.2}}",
					toString(converter, report));
		}
	}

	private String toString(final ProjectileReportToJsonConverter converter, final ProjectileSlacktimeReport report) throws IOException {
		final JSONObject jsonObject = converter.convert(report);
		final StringWriter sw = new StringWriter();
		jsonObject.writeJSONString(sw);
		return sw.toString();
	}
}
