package de.benjaminborbe.authorization.permission;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class PermissionBeanMapperUnitTest {

	@Test
	public void testMapToObject() throws Exception {
		final PermissionBeanMapper mapper = getMapper();
		final String permissionName = "myPermission";
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put("id", permissionName);
			final PermissionBean permissionBean = mapper.map(data);
			assertEquals(permissionName, permissionBean.getId().getId());
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			final PermissionBean permissionBean = mapper.map(data);
			assertNull(permissionBean.getId());
		}
	}

	private PermissionBeanMapper getMapper() {
		final Provider<PermissionBean> permissionBeanProvider = new ProviderMock<PermissionBean>(new PermissionBean());
		final TimeZoneUtil a = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final CurrentTime currentTime = new CurrentTimeImpl();
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CalendarUtil b = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(a, b, parseUtil);
		final MapperPermissionIdentifier mapperPermissionIdentifier = new MapperPermissionIdentifier();
		final PermissionBeanMapper mapper = new PermissionBeanMapper(permissionBeanProvider, mapperCalendar, mapperPermissionIdentifier);
		return mapper;
	}

	@Test
	public void testObjectToMap() throws Exception {
		final PermissionBeanMapper mapper = getMapper();

		final String permissionName = "myPermission";
		{
			final PermissionBean permissionBean = new PermissionBean();
			permissionBean.setId(new PermissionIdentifier(permissionName));
			final Map<String, String> data = mapper.map(permissionBean);
			assertEquals(permissionName, data.get("id"));
		}
		{
			final PermissionBean permissionBean = new PermissionBean();
			final Map<String, String> data = mapper.map(permissionBean);
			assertNull(data.get("id"));
		}
	}
}
