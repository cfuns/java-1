package de.benjaminborbe.virt.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.virt.core.util.MapperVirtIpAddress;
import de.benjaminborbe.virt.core.util.MapperVirtNetworkIdentifier;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class VirtNetworkBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public VirtNetworkBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameterized.Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<Object[]>();
		result.add(new Object[]{VirtNetworkBeanMapper.ID, "1337"});
		result.add(new Object[]{VirtNetworkBeanMapper.NAME, "testNetwork"});
		result.add(new Object[]{VirtNetworkBeanMapper.CREATED, "12345678"});
		result.add(new Object[]{VirtNetworkBeanMapper.MODIFIED, "12345678"});
		result.add(new Object[]{VirtNetworkBeanMapper.IP, "127.0.0.1"});
		return result;
	}

	private VirtNetworkBeanMapper getVirtNetworkBeanMapper() {
		final Provider<VirtNetworkBean> provider = new ProviderMock<VirtNetworkBean>(VirtNetworkBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);

		final MapperVirtNetworkIdentifier mapperVirtNetworkIdentifier = new MapperVirtNetworkIdentifier();
		final MapperString mapperString = new MapperString();
		final MapperVirtIpAddress mapperVirtIpAddress = new MapperVirtIpAddress(parseUtil);
		return new VirtNetworkBeanMapper(provider, mapperString, mapperCalendar, mapperVirtNetworkIdentifier, mapperVirtIpAddress);
	}

	@Test
	public void testMap() throws Exception {
		final VirtNetworkBeanMapper mapper = getVirtNetworkBeanMapper();
		final Map<String, String> inputData = new HashMap<String, String>();
		inputData.put(fieldName, fieldValue);
		final VirtNetworkBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
