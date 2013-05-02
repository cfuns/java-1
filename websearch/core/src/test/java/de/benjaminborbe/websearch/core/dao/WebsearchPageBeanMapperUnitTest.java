package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.websearch.core.util.MapperHttpContent;
import de.benjaminborbe.websearch.core.util.MapperHttpHeader;
import de.benjaminborbe.websearch.core.util.MapperWebsearchPageIdentifier;
import org.apache.commons.codec.binary.Base64;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
public class WebsearchPageBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public WebsearchPageBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameterized.Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() throws UnsupportedEncodingException {
		final List<Object[]> result = new ArrayList<>();
		result.add(new Object[]{WebsearchPageBeanMapper.ID, "1337"});
		result.add(new Object[]{WebsearchPageBeanMapper.LAST_VISIT, "123456"});
		result.add(new Object[]{WebsearchPageBeanMapper.MODIFIED, "123456"});
		result.add(new Object[]{WebsearchPageBeanMapper.CREATED, "123456"});
		result.add(new Object[]{WebsearchPageBeanMapper.URL, "http://www.benjamin-borbe.de"});
		result.add(new Object[]{WebsearchPageBeanMapper.CONTENT, Base64.encodeBase64String("Hello World".getBytes("UTF-8"))});
		result.add(new Object[]{WebsearchPageBeanMapper.HEADER, "{\"keyA\":[\"valueA1\",\"valueA2\"],\"keyB\":[\"valueB1\",\"valueB2\"]}"});
		result.add(new Object[]{WebsearchPageBeanMapper.DURATION, "123456"});
		return result;
	}

	private WebsearchPageBeanMapper getWebsearchPageBeanMapper() {
		final Provider<WebsearchPageBean> beanProvider = new ProviderMock<>(WebsearchPageBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier = new MapperWebsearchPageIdentifier();
		final MapperUrl mapperUrl = new MapperUrl(parseUtil);
		final Base64Util base64Util = new Base64UtilImpl();
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperHttpHeader mapperHttpHeader = new MapperHttpHeader();
		final MapperInteger mapperInteger = new MapperInteger(parseUtil);
		final MapperHttpContent mapperHttpContent = new MapperHttpContent(mapperByteArray);
		final MapperLong mapperLong = new MapperLong(parseUtil);
		return new WebsearchPageBeanMapper(beanProvider, mapperCalendar, mapperWebsearchPageIdentifier, mapperUrl, mapperHttpHeader, mapperInteger, mapperHttpContent, mapperLong);
	}

	@Test
	public void testMapping() throws Exception {
		final WebsearchPageBeanMapper mapper = getWebsearchPageBeanMapper();
		final Map<String, String> inputData = new HashMap<>();
		inputData.put(fieldName, fieldValue);
		final WebsearchPageBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
