package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.StringObjectMapperGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.StringObjectMapperGalleryEntryIdentifier;
import de.benjaminborbe.gallery.util.StringObjectMapperGalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperLong;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryEntryBeanMapper extends MapObjectMapperAdapter<GalleryEntryBean> {

	@Inject
	public GalleryEntryBeanMapper(
			final Provider<GalleryEntryBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final Base64Util base64Util,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, base64Util, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryEntryBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final Base64Util base64Util, final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<GalleryEntryBean>> result = new ArrayList<StringObjectMapper<GalleryEntryBean>>();
		result.add(new StringObjectMapperGalleryEntryIdentifier<GalleryEntryBean>("id"));
		result.add(new StringObjectMapperGalleryCollectionIdentifier<GalleryEntryBean>("collectionId"));
		result.add(new StringObjectMapperGalleryImageIdentifier<GalleryEntryBean>("previewImageIdentifier"));
		result.add(new StringObjectMapperGalleryImageIdentifier<GalleryEntryBean>("imageIdentifier"));
		result.add(new StringObjectMapperString<GalleryEntryBean>("name"));
		result.add(new StringObjectMapperLong<GalleryEntryBean>("priority", parseUtil));
		result.add(new StringObjectMapperBoolean<GalleryEntryBean>("shared", parseUtil));
		result.add(new StringObjectMapperCalendar<GalleryEntryBean>("created", mapperCalendar));
		result.add(new StringObjectMapperCalendar<GalleryEntryBean>("modified", mapperCalendar));
		return result;
	}
}
