package de.benjaminborbe.projectile.util;

import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProjectileCsvReportToDtoConverterUnitTest {

	@Test
	public void testImport() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final Calendar calendar = Calendar.getInstance();

		final ProjectileNameMapper projectileNameMapper = new ProjectileNameMapper(logger);
		final ProjectileCsvReportToDtoConverter converter = new ProjectileCsvReportToDtoConverter(logger, parseUtil, projectileNameMapper);
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		final String csvString = resourceUtil.getResourceContentAsString("sample_report.csv");
		assertThat(converter.convert(calendar, null).size(), is(0));
		assertThat(converter.convert(calendar, "").size(), is(0));
		assertThat(converter.convert(calendar, csvString).size(), is(3));

		assertThat(converter.convert(calendar, csvString).get(0).getUsername(), is("whans"));
		assertThat(converter.convert(calendar, csvString).get(1).getUsername(), is("bfoo"));
		assertThat(converter.convert(calendar, csvString).get(2).getUsername(), is("bgates"));

		assertThat(converter.convert(calendar, csvString).get(0).getExtern(), is(131.25));
		assertThat(converter.convert(calendar, csvString).get(1).getExtern(), is(74.25));
		assertThat(converter.convert(calendar, csvString).get(2).getExtern(), is(138.25));

		assertThat(converter.convert(calendar, csvString).get(0).getIntern(), is(29.5));
		assertThat(converter.convert(calendar, csvString).get(1).getIntern(), is(14.25));
		assertThat(converter.convert(calendar, csvString).get(2).getIntern(), is(28.5));

	}
}
