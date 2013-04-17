package de.benjaminborbe.authentication.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.core.util.MapperUserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.MapperTimeZone;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class UserBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public UserBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<>();
		result.add(new Object[]{UserBeanMapper.ID, "1337"});
		result.add(new Object[]{UserBeanMapper.CREATED, "123456"});
		result.add(new Object[]{UserBeanMapper.MODIFIED, "123456"});
		result.add(new Object[]{UserBeanMapper.PASSWORD, "0123456789abcdef"});
		result.add(new Object[]{UserBeanMapper.PASSWORD_SALT, "0123456789abcdef"});
		result.add(new Object[]{UserBeanMapper.EMAIL, "foo@example.com"});
		result.add(new Object[]{UserBeanMapper.EMAIL_NEW, "foo@example.com"});
		result.add(new Object[]{UserBeanMapper.EMAIL_VERIFIED, "false"});
		result.add(new Object[]{UserBeanMapper.EMAIL_VERIFY_TOKEN, "dsgasb"});
		result.add(new Object[]{UserBeanMapper.FULLNAME, "foo bar"});
		result.add(new Object[]{UserBeanMapper.SUPER_ADMIN, "true"});
		result.add(new Object[]{UserBeanMapper.TIME_ZONE, "Europe/Berlin"});
		result.add(new Object[]{UserBeanMapper.LOGIN_COUNTER, "1337"});
		result.add(new Object[]{UserBeanMapper.LOGIN_DATE, "123456"});
		return result;
	}

	private UserBeanMapper getUserBeanMapper() {
		final Provider<UserBean> taskBeanProvider = new ProviderMock<>(UserBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final Base64Util base64Util = new Base64UtilImpl();
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperTimeZone mapperTimeZone = new MapperTimeZone();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperLong mapperLong = new MapperLong(parseUtil);
		return new UserBeanMapper(taskBeanProvider, mapperUserIdentifier, mapperTimeZone, mapperByteArray, mapperBoolean, mapperString, mapperLong, mapperCalendar);
	}

	@Test
	public void testMap() throws Exception {
		final UserBeanMapper mapper = getUserBeanMapper();
		final Map<String, String> inputData = new HashMap<>();
		inputData.put(fieldName, fieldValue);
		final UserBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}
}
