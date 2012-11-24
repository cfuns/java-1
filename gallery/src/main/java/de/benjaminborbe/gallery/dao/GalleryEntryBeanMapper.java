package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.util.SingleMapGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.SingleMapGalleryEntryIdentifier;
import de.benjaminborbe.gallery.util.SingleMapGalleryImageIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapBoolean;
import de.benjaminborbe.tools.mapper.SingleMapCalendar;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class GalleryEntryBeanMapper extends SingleMappler<GalleryEntryBean> {

	@Inject
	public GalleryEntryBeanMapper(
			final Provider<GalleryEntryBean> provider,
			final ParseUtil parseUtil,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil,
			final Base64Util base64Util) {
		super(provider, buildMappings(parseUtil, timeZoneUtil, calendarUtil, base64Util));
	}

	private static Collection<SingleMap<GalleryEntryBean>> buildMappings(final ParseUtil parseUtil, final TimeZoneUtil timeZoneUtil, final CalendarUtil calendarUtil,
			final Base64Util base64Util) {
		final List<SingleMap<GalleryEntryBean>> result = new ArrayList<SingleMap<GalleryEntryBean>>();
		result.add(new SingleMapGalleryEntryIdentifier<GalleryEntryBean>("id"));
		result.add(new SingleMapGalleryCollectionIdentifier<GalleryEntryBean>("collectionId"));
		result.add(new SingleMapGalleryImageIdentifier<GalleryEntryBean>("previewImageIdentifier"));
		result.add(new SingleMapGalleryImageIdentifier<GalleryEntryBean>("imageIdentifier"));
		result.add(new SingleMapString<GalleryEntryBean>("name"));
		result.add(new SingleMapBoolean<GalleryEntryBean>("enabled", parseUtil));
		result.add(new SingleMapCalendar<GalleryEntryBean>("created", timeZoneUtil, calendarUtil, parseUtil));
		result.add(new SingleMapCalendar<GalleryEntryBean>("modified", timeZoneUtil, calendarUtil, parseUtil));
		return result;
	}
}
