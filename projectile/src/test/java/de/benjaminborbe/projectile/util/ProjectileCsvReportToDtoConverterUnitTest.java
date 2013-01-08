package de.benjaminborbe.projectile.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import de.benjaminborbe.tools.util.StreamUtil;

public class ProjectileCsvReportToDtoConverterUnitTest {

	@Test
	public void testImport() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = new ParseUtilImpl();

		final ProjectileNameMapper projectileNameMapper = new ProjectileNameMapper(logger);
		final ProjectileCsvReportToDtoConverter converter = new ProjectileCsvReportToDtoConverter(logger, parseUtil, projectileNameMapper);
		final StreamUtil streamUtil = new StreamUtil();
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		final String csvString = resourceUtil.getResourceContentAsString("sample_report.csv");
		assertThat(converter.convert(null).size(), is(0));
		assertThat(converter.convert("").size(), is(0));
		assertThat(converter.convert(csvString).size(), is(2));

		assertThat(converter.convert(csvString).get(0).getUsername(), is("whans"));
		assertThat(converter.convert(csvString).get(1).getUsername(), is("bfoo"));

		assertThat(converter.convert(csvString).get(0).getExtern(), is(131.25));
		assertThat(converter.convert(csvString).get(1).getExtern(), is(74.25));

		assertThat(converter.convert(csvString).get(0).getIntern(), is(29.5));
		assertThat(converter.convert(csvString).get(1).getIntern(), is(14.25));

	}
}
