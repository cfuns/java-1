package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryCollectionIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryEntryIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryImageIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class GalleryEntryBeanMapper extends MapObjectMapperAdapter<GalleryEntryBean> {

	@Inject
	public GalleryEntryBeanMapper(
		final Provider<GalleryEntryBean> provider,
		final MapperGalleryEntryIdentifier mapperGalleryEntryIdentifier,
		final MapperGalleryCollectionIdentifier mapperGalleryCollectionIdentifier,
		final MapperGalleryImageIdentifier mapperGalleryImageIdentifier,
		final MapperString mapperString,
		final MapperLong mapperLong,
		final MapperBoolean mapperBoolean,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperGalleryEntryIdentifier, mapperGalleryCollectionIdentifier, mapperGalleryImageIdentifier, mapperString, mapperLong, mapperBoolean,
			mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryEntryBean>> buildMappings(
		final MapperGalleryEntryIdentifier mapperGalleryEntryIdentifier,
		final MapperGalleryCollectionIdentifier mapperGalleryCollectionIdentifier,
		final MapperGalleryImageIdentifier mapperGalleryImageIdentifier,
		final MapperString mapperString,
		final MapperLong mapperLong,
		final MapperBoolean mapperBoolean,
		final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<GalleryEntryBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, GalleryEntryIdentifier>("id", mapperGalleryEntryIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, GalleryCollectionIdentifier>("collectionId", mapperGalleryCollectionIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, GalleryImageIdentifier>("previewImageIdentifier", mapperGalleryImageIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, GalleryImageIdentifier>("imageIdentifier", mapperGalleryImageIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, Long>("priority", mapperLong));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, Boolean>("shared", mapperBoolean));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<GalleryEntryBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
