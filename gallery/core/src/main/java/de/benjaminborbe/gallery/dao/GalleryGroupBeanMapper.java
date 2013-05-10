package de.benjaminborbe.gallery.dao;

import com.google.inject.Provider;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.util.MapperGalleryGroupIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
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
public class GalleryGroupBeanMapper extends MapObjectMapperAdapter<GalleryGroupBean> {

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String SHARED = "shared";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String SHORT_SIDE_MIN_LENGTH = "shortSideMinLength";

	public static final String SHORT_SIDE_MAX_LENGTH = "shortSideMaxLength";

	public static final String LONG_SIDE_MIN_LENGTH = "longSideMinLength";

	public static final String LONG_SIDE_MAX_LENGTH = "longSideMaxLength";

	public static final String PREVIEW_SHORT_SIDE_MIN_LENGTH = "previewShortSideMinLength";

	public static final String PREVIEW_SHORT_SIDE_MAX_LENGTH = "previewShortSideMaxLength";

	public static final String PREVIEW_LONG_SIDE_MIN_LENGTH = "previewLongSideMinLength";

	public static final String PREVIEW_LONG_SIDE_MAX_LENGTH = "previewLongSideMaxLength";

	@Inject
	public GalleryGroupBeanMapper(
		final Provider<GalleryGroupBean> provider,
		final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier,
		final MapperString mapperString,
		final MapperBoolean mapperBoolean,
		final MapperCalendar mapperCalendar,
		final MapperInteger mapperInteger
	) {
		super(provider, buildMappings(mapperGalleryGroupIdentifier, mapperString, mapperBoolean, mapperCalendar, mapperInteger));
	}

	private static Collection<StringObjectMapper<GalleryGroupBean>> buildMappings(
		final MapperGalleryGroupIdentifier mapperGalleryGroupIdentifier,
		final MapperString mapperString,
		final MapperBoolean mapperBoolean,
		final MapperCalendar mapperCalendar,
		final MapperInteger mapperInteger
	) {
		final List<StringObjectMapper<GalleryGroupBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, GalleryGroupIdentifier>(ID, mapperGalleryGroupIdentifier));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Boolean>(SHARED, mapperBoolean));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(SHORT_SIDE_MIN_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(SHORT_SIDE_MAX_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(LONG_SIDE_MIN_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(LONG_SIDE_MAX_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(PREVIEW_SHORT_SIDE_MIN_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(PREVIEW_SHORT_SIDE_MAX_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(PREVIEW_LONG_SIDE_MIN_LENGTH, mapperInteger));
		result.add(new StringObjectMapperAdapter<GalleryGroupBean, Integer>(PREVIEW_LONG_SIDE_MAX_LENGTH, mapperInteger));
		return result;
	}
}
