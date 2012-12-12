package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.SingleMapGalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapByteArray;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryImageBeanMapper extends SingleMappler<GalleryImageBean> {

	@Inject
	public GalleryImageBeanMapper(
			final Provider<GalleryImageBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final Base64Util base64Util) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, base64Util));
	}

	private static Collection<SingleMap<GalleryImageBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final Base64Util base64Util) {
		final List<SingleMap<GalleryImageBean>> result = new ArrayList<SingleMap<GalleryImageBean>>();
		result.add(new SingleMapGalleryImageIdentifier<GalleryImageBean>("id"));
		result.add(new SingleMapString<GalleryImageBean>("contentType"));
		result.add(new SingleMapString<GalleryImageBean>("name"));
		result.add(new SingleMapByteArray<GalleryImageBean>("content", base64Util));
		result.add(new SingleMapCalendar<GalleryImageBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<GalleryImageBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
