package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GalleryImageBeanMapperUnitTest {

	private GalleryImageBeanMapper getGalleryImageBeanMapper() {
		final Provider<GalleryImageBean> taskBeanProvider = new ProviderMock<GalleryImageBean>(GalleryImageBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final Base64Util base64Util = new Base64UtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperByteArray mapperByteArray = new MapperByteArray(base64Util);
		final MapperString mapperString = new MapperString();
		final MapperGalleryImageIdentifier mapperGalleryImageIdentifier = new MapperGalleryImageIdentifier();
		return new GalleryImageBeanMapper(taskBeanProvider, mapperGalleryImageIdentifier, mapperString, mapperByteArray, mapperCalendar);
	}

	@Test
	public void testId() throws Exception {
		final GalleryImageBeanMapper mapper = getGalleryImageBeanMapper();
		final GalleryImageBean bean = new GalleryImageBean();
		final byte[] content = "bla".getBytes();
		final String contentType = "jpeg";
		final Calendar created = Calendar.getInstance();
		final Calendar modified = Calendar.getInstance();
		final GalleryImageIdentifier id = new GalleryImageIdentifier("123");
		bean.setId(id);
		bean.setContent(content);
		bean.setContentType(contentType);
		bean.setCreated(created);
		bean.setModified(modified);
		final Map<String, String> data = mapper.map(bean);
		final GalleryImageBean newBean = mapper.map(data);
		assertEquals(id, newBean.getId());
		assertEquals(created.getTimeInMillis(), newBean.getCreated().getTimeInMillis());
		assertEquals(modified.getTimeInMillis(), newBean.getModified().getTimeInMillis());
		assertEquals(new String(content), new String(newBean.getContent()));
		assertEquals(contentType, newBean.getContentType());
	}
}
