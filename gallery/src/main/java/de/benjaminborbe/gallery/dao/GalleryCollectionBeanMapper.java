package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.SingleMapGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.SingleMapGalleryGroupIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapLong;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryCollectionBeanMapper extends SingleMappler<GalleryCollectionBean> {

	@Inject
	public GalleryCollectionBeanMapper(final Provider<GalleryCollectionBean> provider, final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil));
	}

	private static Collection<SingleMap<GalleryCollectionBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil) {
		final List<SingleMap<GalleryCollectionBean>> result = new ArrayList<SingleMap<GalleryCollectionBean>>();
		result.add(new SingleMapGalleryCollectionIdentifier<GalleryCollectionBean>("id"));
		result.add(new SingleMapGalleryGroupIdentifier<GalleryCollectionBean>("groupId"));
		result.add(new SingleMapString<GalleryCollectionBean>("name"));
		result.add(new SingleMapLong<GalleryCollectionBean>("priority", parseUtil));
		result.add(new SingleMapCalendar<GalleryCollectionBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<GalleryCollectionBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
