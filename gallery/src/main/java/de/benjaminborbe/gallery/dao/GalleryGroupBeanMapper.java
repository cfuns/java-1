package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.StringObjectMapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryGroupBeanMapper extends MapObjectMapperAdapter<GalleryGroupBean> {

	@Inject
	public GalleryGroupBeanMapper(
			final Provider<GalleryGroupBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryGroupBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<GalleryGroupBean>> result = new ArrayList<StringObjectMapper<GalleryGroupBean>>();
		result.add(new StringObjectMapperGalleryGroupIdentifier<GalleryGroupBean>("id"));
		result.add(new StringObjectMapperString<GalleryGroupBean>("name"));
		result.add(new StringObjectMapperBoolean<GalleryGroupBean>("shared", parseUtil));
		result.add(new StringObjectMapperCalendar<GalleryGroupBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<GalleryGroupBean>("modified", mapperCalendar));
		return result;
	}
}
