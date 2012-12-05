package de.benjaminborbe.confluence.dao;

import static org.junit.Assert.assertEquals;

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
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class ConfluenceInstanceBeanMapperUnitTest {

	private ConfluenceInstanceBeanMapper getConfluenceInstanceBeanMapper() {
		final Provider<ConfluenceInstanceBean> confluenceInstanceBeanProvider = new Provider<ConfluenceInstanceBean>() {

			@Override
			public ConfluenceInstanceBean get() {
				return new ConfluenceInstanceBean();
			}
		};

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		return new ConfluenceInstanceBeanMapper(confluenceInstanceBeanProvider, parseUtil, timeZoneUtil, calendarUtil);
	}

	@Test
	public void testOwnerMapping() throws Exception {
		final ConfluenceInstanceBeanMapper mapper = getConfluenceInstanceBeanMapper();
		final UserIdentifier value = new UserIdentifier("1337");
		final String fieldname = "owner";
		{
			final ConfluenceInstanceBean bean = new ConfluenceInstanceBean();
			bean.setOwner(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final ConfluenceInstanceBean bean = mapper.map(data);
			assertEquals(value, bean.getOwner());
		}
	}
}
