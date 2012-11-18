package de.benjaminborbe.gallery.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class GalleryEntryBeanMapperUnitTest {

	private GalleryEntryBeanMapper getGalleryEntryBeanMapper() {
		final Provider<GalleryEntryBean> taskBeanProvider = new Provider<GalleryEntryBean>() {

			@Override
			public GalleryEntryBean get() {
				return new GalleryEntryBean();
			}
		};

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final Base64Util a = new Base64UtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		return new GalleryEntryBeanMapper(taskBeanProvider, parseUtil, timeZoneUtil, calendarUtil, a);
	}

	@Test
	public void testId() throws Exception {
		final GalleryEntryBeanMapper mapper = getGalleryEntryBeanMapper();
		final GalleryEntryBean bean = new GalleryEntryBean();
		final GalleryEntryIdentifier id = new GalleryEntryIdentifier("1337");
		final String name = "test123";
		final GalleryCollectionIdentifier galleryIdentifier = new GalleryCollectionIdentifier("1337");
		final Calendar created = Calendar.getInstance();
		final Calendar modified = Calendar.getInstance();
		final GalleryImageIdentifier previewImageIdentifier = new GalleryImageIdentifier("1337");
		final GalleryImageIdentifier imageIdentifier = new GalleryImageIdentifier("1337");
		bean.setId(id);
		bean.setName(name);
		bean.setCreated(created);
		bean.setModified(modified);
		bean.setPreviewImageIdentifier(previewImageIdentifier);
		bean.setImageIdentifier(imageIdentifier);
		bean.setCollectionId(galleryIdentifier);
		final Map<String, String> data = mapper.map(bean);
		final GalleryEntryBean newBean = mapper.map(data);
		assertEquals(id, newBean.getId());
		assertEquals(name, newBean.getName());
		assertEquals(created.getTimeInMillis(), newBean.getCreated().getTimeInMillis());
		assertEquals(modified.getTimeInMillis(), newBean.getModified().getTimeInMillis());
		assertEquals(previewImageIdentifier, newBean.getPreviewImageIdentifier());
		assertEquals(imageIdentifier, newBean.getImageIdentifier());
		assertEquals(galleryIdentifier, newBean.getCollectionId());

	}
}
