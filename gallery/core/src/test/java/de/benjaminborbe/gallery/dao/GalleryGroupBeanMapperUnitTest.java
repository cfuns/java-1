package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.util.MapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
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
public class GalleryGroupBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public GalleryGroupBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameterized.Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<>();
		result.add(new Object[]{GalleryGroupBeanMapper.ID, "1337"});
		result.add(new Object[]{GalleryGroupBeanMapper.CREATED, "1337"});
		result.add(new Object[]{GalleryGroupBeanMapper.MODIFIED, "1337"});
		result.add(new Object[]{GalleryGroupBeanMapper.NAME, "foobar"});
		result.add(new Object[]{GalleryGroupBeanMapper.SHARED, "true"});
		result.add(new Object[]{GalleryGroupBeanMapper.LONG_SIDE_MAX_LENGTH, "1"});
		result.add(new Object[]{GalleryGroupBeanMapper.LONG_SIDE_MIN_LENGTH, "2"});
		result.add(new Object[]{GalleryGroupBeanMapper.SHORT_SIDE_MAX_LENGTH, "3"});
		result.add(new Object[]{GalleryGroupBeanMapper.SHORT_SIDE_MIN_LENGTH, "4"});
		return result;
	}

	private GalleryGroupBeanMapper getGalleryGroupBeanMapper() {
		final Provider<GalleryGroupBean> provider = new ProviderMock<>(GalleryGroupBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);

		final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier = new MapperGalleryGroupIdentifier();
		final MapperString mapperString = new MapperString();
		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperInteger mapperInteger = new MapperInteger(parseUtil);
		return new GalleryGroupBeanMapper(provider, mapperGalleryGroupIdentifier, mapperString, mapperBoolean, mapperCalendar, mapperInteger);
	}

	@Test
	public void testMap() throws Exception {
		final GalleryGroupBeanMapper mapper = getGalleryGroupBeanMapper();
		final Map<String, String> inputData = new HashMap<>();
		inputData.put(fieldName, fieldValue);
		final GalleryGroupBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
