package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.SingleMapGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapBoolean;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryGroupBeanMapper extends SingleMappler<GalleryGroupBean> {

	@Inject
	public GalleryGroupBeanMapper(final Provider<GalleryGroupBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<GalleryGroupBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<GalleryGroupBean>> result = new ArrayList<SingleMap<GalleryGroupBean>>();
		result.add(new SingleMapGalleryGroupIdentifier<GalleryGroupBean>("id"));
		result.add(new SingleMapString<GalleryGroupBean>("name"));
		result.add(new SingleMapBoolean<GalleryGroupBean>("shared", parseUtil));
		result.add(new SingleMapCalendar<GalleryGroupBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<GalleryGroupBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
