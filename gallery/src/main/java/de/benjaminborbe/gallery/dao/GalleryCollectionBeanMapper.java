package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.StringObjectMapperGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.StringObjectMapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperLong;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryCollectionBeanMapper extends MapObjectMapperAdapter<GalleryCollectionBean> {

	@Inject
	public GalleryCollectionBeanMapper(
			final Provider<GalleryCollectionBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryCollectionBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<GalleryCollectionBean>> result = new ArrayList<StringObjectMapper<GalleryCollectionBean>>();
		result.add(new StringObjectMapperGalleryCollectionIdentifier<GalleryCollectionBean>("id"));
		result.add(new StringObjectMapperGalleryGroupIdentifier<GalleryCollectionBean>("groupId"));
		result.add(new StringObjectMapperString<GalleryCollectionBean>("name"));
		result.add(new StringObjectMapperLong<GalleryCollectionBean>("priority", parseUtil));
		result.add(new StringObjectMapperBoolean<GalleryCollectionBean>("shared", parseUtil));
		result.add(new StringObjectMapperCalendar<GalleryCollectionBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<GalleryCollectionBean>("modified", mapperCalendar));
		return result;
	}
}
