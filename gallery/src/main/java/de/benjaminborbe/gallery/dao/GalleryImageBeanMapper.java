package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.StringObjectMapperGalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperByteArray;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryImageBeanMapper extends MapObjectMapperAdapter<GalleryImageBean> {

	@Inject
	public GalleryImageBeanMapper(
			final Provider<GalleryImageBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final Base64Util base64Util,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, base64Util, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryImageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final Base64Util base64Util, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<GalleryImageBean>> result = new ArrayList<StringObjectMapper<GalleryImageBean>>();
		result.add(new StringObjectMapperGalleryImageIdentifier<GalleryImageBean>("id"));
		result.add(new StringObjectMapperString<GalleryImageBean>("contentType"));
		result.add(new StringObjectMapperString<GalleryImageBean>("name"));
		result.add(new StringObjectMapperByteArray<GalleryImageBean>("content", base64Util));
		result.add(new StringObjectMapperCalendar<GalleryImageBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<GalleryImageBean>("modified", mapperCalendar));
		return result;
	}
}
