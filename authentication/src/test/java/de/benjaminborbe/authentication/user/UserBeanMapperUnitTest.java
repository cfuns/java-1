package de.benjaminborbe.authentication.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class UserBeanMapperUnitTest {

	private UserBeanMapper getUserBeanMapper() {
		final Provider<UserBean> taskBeanProvider = new Provider<UserBean>() {

			@Override
			public UserBean get() {
				return new UserBean();
			}
		};

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final Base64Util base64Util = new Base64UtilImpl();
		return new UserBeanMapper(taskBeanProvider, parseUtil, calendarUtil, base64Util);
	}

	@Test
	public void testId() throws Exception {
		final UserBeanMapper mapper = getUserBeanMapper();
		final UserIdentifier value = new UserIdentifier("1337");
		final String fieldname = "id";
		{
			final UserBean bean = new UserBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final UserBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}

	@Test
	public void testPassword() throws Exception {
		final Base64Util base64Util = new Base64UtilImpl();
		final UserBeanMapper mapper = getUserBeanMapper();
		final byte[] value = "god".getBytes();
		final String valueString = base64Util.encode(value);
		final String fieldname = "password";
		{
			final UserBean bean = new UserBean();
			bean.setPassword(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), valueString);
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, valueString);
			final UserBean bean = mapper.map(data);
			assertTrue(Arrays.equals(value, bean.getPassword()));
		}
	}

	@Test
	public void testSuperAdmin() throws Exception {
		final UserBeanMapper mapper = getUserBeanMapper();
		final Boolean value = true;
		final String fieldname = "superAdmin";
		{
			final UserBean bean = new UserBean();
			bean.setSuperAdmin(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final UserBean bean = mapper.map(data);
			assertEquals(value, bean.getSuperAdmin());
		}
	}
}
