package de.benjaminborbe.projectile.dao;

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

import de.benjaminborbe.projectile.util.MapperProjectileReportIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperDouble;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

@RunWith(Parameterized.class)
public class ProjectileReportBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public ProjectileReportBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<Object[]>();
		result.add(new Object[] { "id", "1337" });
		result.add(new Object[] { "created", "123456" });
		result.add(new Object[] { "modified", "123456" });
		result.add(new Object[] { "name", "test" });

		result.add(new Object[] { "weekIntern", "12.34" });
		result.add(new Object[] { "weekExtern", "12.34" });
		result.add(new Object[] { "weekTarget", "12.34" });
		result.add(new Object[] { "weekBillable", "12.34" });
		result.add(new Object[] { "weekUpdateDate", "123456" });

		result.add(new Object[] { "monthIntern", "12.34" });
		result.add(new Object[] { "monthExtern", "12.34" });
		result.add(new Object[] { "monthTarget", "12.34" });
		result.add(new Object[] { "monthBillable", "12.34" });
		result.add(new Object[] { "monthUpdateDate", "123456" });

		result.add(new Object[] { "yearIntern", "12.34" });
		result.add(new Object[] { "yearExtern", "12.34" });
		result.add(new Object[] { "yearTarget", "12.34" });
		result.add(new Object[] { "yearBillable", "12.34" });
		result.add(new Object[] { "yearUpdateDate", "123456" });

		return result;
	}

	private ProjectileReportBeanMapper getProjectileReportBeanMapper() {
		final Provider<ProjectileReportBean> provider = new ProviderMock<ProjectileReportBean>(ProjectileReportBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);

		final MapperString mapperString = new MapperString();
		final MapperProjectileReportIdentifier mapperProjectileReportIdentifier = new MapperProjectileReportIdentifier();
		final MapperDouble mapperDouble = new MapperDouble(parseUtil);
		return new ProjectileReportBeanMapper(provider, mapperProjectileReportIdentifier, mapperString, mapperCalendar, mapperDouble);
	}

	@Test
	public void testMap() throws Exception {
		final ProjectileReportBeanMapper mapper = getProjectileReportBeanMapper();
		final Map<String, String> inputData = new HashMap<String, String>();
		inputData.put(fieldName, fieldValue);
		final ProjectileReportBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
