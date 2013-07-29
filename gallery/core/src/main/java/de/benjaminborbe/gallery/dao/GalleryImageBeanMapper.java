package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryImageIdentifier;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
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
public class GalleryImageBeanMapper extends MapObjectMapperAdapter<GalleryImageBean> {

	@Inject
	public GalleryImageBeanMapper(
		final Provider<GalleryImageBean> provider,
		final MapperGalleryImageIdentifier mapperGalleryImageIdentifier,
		final MapperString mapperString,
		final MapperByteArray mapperByteArray,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperGalleryImageIdentifier, mapperString, mapperByteArray, mapperCalendar));
	}

	private static Collection<StringObjectMapper<GalleryImageBean>> buildMappings(
		final MapperGalleryImageIdentifier mapperGalleryImageIdentifier, final MapperString mapperString,
		final MapperByteArray mapperByteArray, final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<GalleryImageBean>> result = new ArrayList<StringObjectMapper<GalleryImageBean>>();
		result.add(new StringObjectMapperAdapter<GalleryImageBean, GalleryImageIdentifier>("id", mapperGalleryImageIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryImageBean, String>("contentType", mapperString));
		result.add(new StringObjectMapperAdapter<GalleryImageBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<GalleryImageBean, byte[]>("content", mapperByteArray));
		result.add(new StringObjectMapperAdapter<GalleryImageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<GalleryImageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
