package de.benjaminborbe.bookmark.dao;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.bookmark.util.MapperBookmarkIdentifier;
import de.benjaminborbe.bookmark.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperListString;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class BookmarkBeanMapper extends MapObjectMapperAdapter<BookmarkBean> {

	@Inject
	public BookmarkBeanMapper(
		final Provider<BookmarkBean> provider,
		final MapperBookmarkIdentifier mapperBookmarkIdentifier,
		final MapperString mapperString,
		final MapperBoolean mapperBoolean,
		final MapperCalendar mapperCalendar,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperListString mapperListString) {
		super(provider, buildMappings(mapperBookmarkIdentifier, mapperString, mapperBoolean, mapperCalendar, mapperUserIdentifier, mapperListString));
	}

	private static Collection<StringObjectMapper<BookmarkBean>> buildMappings(final MapperBookmarkIdentifier mapperBookmarkIdentifier, final MapperString mapperString,
																																						final MapperBoolean mapperBoolean, final MapperCalendar mapperCalendar, final MapperUserIdentifier mapperUserIdentifier, final MapperListString mapperListString) {
		final List<StringObjectMapper<BookmarkBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<BookmarkBean, BookmarkIdentifier>("id", mapperBookmarkIdentifier));
		result.add(new StringObjectMapperAdapter<BookmarkBean, UserIdentifier>("owner", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<BookmarkBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<BookmarkBean, String>("url", mapperString));
		result.add(new StringObjectMapperAdapter<BookmarkBean, String>("description", mapperString));
		result.add(new StringObjectMapperAdapter<BookmarkBean, Boolean>("favorite", mapperBoolean));
		result.add(new StringObjectMapperAdapter<BookmarkBean, List<String>>("keywords", mapperListString));
		result.add(new StringObjectMapperAdapter<BookmarkBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<BookmarkBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
