package de.benjaminborbe.filestorage.dao;

import com.google.inject.Provider;
import de.benjaminborbe.filestorage.util.MapperFilestorageEntryIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.apache.commons.codec.binary.Base64;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class FilestorageEntryMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public FilestorageEntryMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() throws UnsupportedEncodingException {
		final List<Object[]> result = new ArrayList<>();
		result.add(new Object[]{FilestorageEntryBeanMapper.ID, "1337"});
		result.add(new Object[]{FilestorageEntryBeanMapper.CREATED, "123456"});
		result.add(new Object[]{FilestorageEntryBeanMapper.MODIFIED, "123456"});
		result.add(new Object[]{FilestorageEntryBeanMapper.CONTENT, Base64.encodeBase64String("Hello World".getBytes("UTF-8"))});
		result.add(new Object[]{FilestorageEntryBeanMapper.CONTENT_TYPE, "text/plain"});
		result.add(new Object[]{FilestorageEntryBeanMapper.FILENAME, "foobar.txt"});
		return result;
	}

	private FilestorageEntryBeanMapper getFilestorageEntryBeanMapper() {
		final Provider<FilestorageEntryBean> beanProvider = new ProviderMock<>(FilestorageEntryBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier = new MapperFilestorageEntryIdentifier();
		final Base64UtilImpl base64Util = new Base64UtilImpl();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		return new FilestorageEntryBeanMapper(beanProvider, mapperCalendar, mapperFilestorageEntryIdentifier, mapperString, mapperByteArray);
	}

	@Test
	public void testMapping() throws Exception {
		final FilestorageEntryBeanMapper mapper = getFilestorageEntryBeanMapper();
		final Map<String, String> inputData = new HashMap<>();
		inputData.put(fieldName, fieldValue);
		final FilestorageEntryBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
