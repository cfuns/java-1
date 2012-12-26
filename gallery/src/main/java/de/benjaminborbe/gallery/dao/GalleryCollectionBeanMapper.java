package de.benjaminborbe.gallery.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class GalleryCollectionBeanMapper extends MapObjectMapperAdapter<GalleryCollectionBean> {

	@Inject
	public GalleryCollectionBeanMapper(
			final Provider<GalleryCollectionBean> provider,
			final MapperGalleryCollectionIdentifier mapperGalleryCollectionIdentifier,
			final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier,
			final MapperString mapperString,
			final MapperLong mapperLong,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperGalleryCollectionIdentifier, mapperGalleryGroupIdentifier, mapperString, mapperLong, mapperBoolean, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryCollectionBean>> buildMappings(final MapperGalleryCollectionIdentifier mapperGalleryCollectionIdentifier,
			final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier, final MapperString mapperString, final MapperLong mapperLong, final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<GalleryCollectionBean>> result = new ArrayList<StringObjectMapper<GalleryCollectionBean>>();
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, GalleryCollectionIdentifier>("id", mapperGalleryCollectionIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, GalleryGroupIdentifier>("groupId", mapperGalleryGroupIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, Long>("priority", mapperLong));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, Boolean>("shared", mapperBoolean));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<GalleryCollectionBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
