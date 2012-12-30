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
import de.benjaminborbe.authorization.dao.PermissionBean;
import de.benjaminborbe.authorization.dao.PermissionBeanMapper;
import de.benjaminborbe.authorization.util.MapperPermissionIdentifier;
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
		final String permissionName = "myPermission";
		{
			final PermissionBeanMapper mapper = getMapper();
			final Map<String, String> data = new HashMap<String, String>();
			data.put("id", permissionName);
			final PermissionBean permissionBean = mapper.map(data);
			assertEquals(permissionName, permissionBean.getId().getId());
		}
		{
			final PermissionBeanMapper mapper = getMapper();
			final Map<String, String> data = new HashMap<String, String>();
			final PermissionBean permissionBean = mapper.map(data);
			assertNull(permissionBean.getId());
		}
	}

	private PermissionBeanMapper getMapper() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Provider<PermissionBean> permissionBeanProvider = new ProviderMock<PermissionBean>(PermissionBean.class);
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperPermissionIdentifier mapperPermissionIdentifier = new MapperPermissionIdentifier();
		return new PermissionBeanMapper(permissionBeanProvider, mapperCalendar, mapperPermissionIdentifier);
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
