package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GalleryCollectionBeanMapperUnitTest {

	private GalleryCollectionBeanMapper getGalleryCollectionBeanMapper() {
		final Provider<GalleryCollectionBean> taskBeanProvider = new ProviderMock<>(GalleryCollectionBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);

		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperLong mapperLong = new MapperLong(parseUtil);
		final MapperGalleryCollectionIdentifier mapperGalleryCollectionIdentifier = new MapperGalleryCollectionIdentifier();
		final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier = new MapperGalleryGroupIdentifier();
		return new GalleryCollectionBeanMapper(taskBeanProvider, mapperGalleryCollectionIdentifier, mapperGalleryGroupIdentifier, mapperString, mapperLong, mapperBoolean,
			mapperCalendar);
	}

	@Test
	public void testId() throws Exception {
		final GalleryCollectionBeanMapper mapper = getGalleryCollectionBeanMapper();
		final GalleryCollectionBean bean = new GalleryCollectionBean();
		final GalleryCollectionIdentifier id = new GalleryCollectionIdentifier("1337");
		final String name = "test123";
		final Calendar created = Calendar.getInstance();
		final Calendar modified = Calendar.getInstance();
		bean.setId(id);
		bean.setName(name);
		bean.setCreated(created);
		bean.setModified(modified);
		final Map<String, String> data = mapper.map(bean);
		final GalleryCollectionBean newBean = mapper.map(data);
		assertEquals(id, newBean.getId());
		assertEquals(name, newBean.getName());
		assertEquals(created.getTimeInMillis(), newBean.getCreated().getTimeInMillis());
		assertEquals(modified.getTimeInMillis(), newBean.getModified().getTimeInMillis());
	}
}
