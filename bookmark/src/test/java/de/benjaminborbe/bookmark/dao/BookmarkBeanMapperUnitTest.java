package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.jsoup.helper.StringUtil;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperListString;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class BookmarkBeanMapperUnitTest {

	@Test
	public void testId() throws Exception {

		final List<List<String>> values = new ArrayList<List<String>>();
		values.add(new ArrayList<String>());
		values.add(Arrays.asList("a"));
		values.add(Arrays.asList("a", "b"));
		for (final List<String> value : values) {

			final BookmarkBeanMapper mapper = getBookmarkBeanMapper();
			final String fieldname = "keywords";
			{
				final BookmarkBean bean = new BookmarkBean();
				bean.setKeywords(value);
				final Map<String, String> data = mapper.map(bean);
				assertEquals(data.get(fieldname), value.isEmpty() ? null : StringUtil.join(value, ","));
			}
			{
				final Map<String, String> data = new HashMap<String, String>();
				data.put(fieldname, value.isEmpty() ? null : StringUtil.join(value, ","));
				final BookmarkBean bean = mapper.map(data);
				assertEquals(value.size(), bean.getKeywords().size());
				assertEquals(StringUtils.join(value, ","), StringUtils.join(bean.getKeywords(), ","));
			}
		}
	}

	private BookmarkBeanMapper getBookmarkBeanMapper() {
		final Provider<BookmarkBean> provider = new ProviderMock<BookmarkBean>(BookmarkBean.class);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final MapperBookmarkIdentifier mapperBookmarkIdentifier = new MapperBookmarkIdentifier();
		final MapperString mapperString = new MapperString();
		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperListString mapperListString = new MapperListString();

		return new BookmarkBeanMapper(provider, mapperBookmarkIdentifier, mapperString, mapperBoolean, mapperCalendar, mapperUserIdentifier, mapperListString);
	}
}
