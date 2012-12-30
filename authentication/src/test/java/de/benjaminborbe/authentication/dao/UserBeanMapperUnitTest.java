package de.benjaminborbe.authentication.dao;

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
import de.benjaminborbe.authentication.dao.UserBean;
import de.benjaminborbe.authentication.dao.UserBeanMapper;
import de.benjaminborbe.authentication.util.MapperUserIdentifier;
import de.benjaminborbe.tools.date.CurrentTime;
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

public class UserBeanMapperUnitTest {

	private UserBeanMapper getUserBeanMapper() {
		final Provider<UserBean> taskBeanProvider = new ProviderMock<UserBean>(UserBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final Base64Util base64Util = new Base64UtilImpl();

		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperTimeZone mapperTimeZone = new MapperTimeZone();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperCalendar mapperCalendar = new MapperCalendar(null, null, parseUtil);
		final MapperLong mapperLong = new MapperLong(parseUtil);
		return new UserBeanMapper(taskBeanProvider, mapperUserIdentifier, mapperTimeZone, mapperByteArray, mapperBoolean, mapperString, mapperLong, mapperCalendar);
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
