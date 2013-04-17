package de.benjaminborbe.note.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.note.util.MapperNoteIdentifier;
import de.benjaminborbe.note.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class NoteBeanMapper extends MapObjectMapperAdapter<NoteBean> {

	public static final String ID = "id";

	public static final String OWNER = "owner";

	public static final String TITLE = "title";

	public static final String CONTENT = "content";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	@Inject
	public NoteBeanMapper(
		final Provider<NoteBean> provider,
		final MapperNoteIdentifier mapperNoteIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperString mapperString,
		final MapperUserIdentifier mapperUserIdentifier) {
		super(provider, buildMappings(mapperUserIdentifier, mapperNoteIdentifier, mapperCalendar, mapperString));
	}

	private static Collection<StringObjectMapper<NoteBean>> buildMappings(final MapperUserIdentifier mapperUserIdentifier, final MapperNoteIdentifier mapperNoteIdentifier,
																																				final MapperCalendar mapperCalendar, final MapperString mapperString) {
		final List<StringObjectMapper<NoteBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<NoteBean, NoteIdentifier>(ID, mapperNoteIdentifier));
		result.add(new StringObjectMapperAdapter<NoteBean, UserIdentifier>(OWNER, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<NoteBean, String>(TITLE, mapperString));
		result.add(new StringObjectMapperAdapter<NoteBean, String>(CONTENT, mapperString));
		result.add(new StringObjectMapperAdapter<NoteBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<NoteBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
