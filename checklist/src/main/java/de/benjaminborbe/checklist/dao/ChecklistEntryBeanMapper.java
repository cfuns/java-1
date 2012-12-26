package de.benjaminborbe.checklist.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class ChecklistEntryBeanMapper extends MapObjectMapperAdapter<ChecklistEntryBean> {

	public static final String LIST_ID = "listId";

	public static final String OWNER = "owner";

	@Inject
	public ChecklistEntryBeanMapper(
			final Provider<ChecklistEntryBean> provider,
			final MapperEntryIdentifier mapperEntryIdentifier,
			final MapperListIdentifier mapperListIdentifier,
			final MapperUserIdentifier mapperUserIdentifier,
			final MapperString mapperString,
			final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperEntryIdentifier, mapperListIdentifier, mapperUserIdentifier, mapperString, mapperBoolean, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ChecklistEntryBean>> buildMappings(final MapperEntryIdentifier mapperEntryIdentifier,
			final MapperListIdentifier mapperListIdentifier, final MapperUserIdentifier mapperUserIdentifier, final MapperString mapperString, final MapperBoolean mapperBoolean,
			final MapperCalendar mapperCalendar) {
		final List<StringObjectMapper<ChecklistEntryBean>> result = new ArrayList<StringObjectMapper<ChecklistEntryBean>>();
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, ChecklistEntryIdentifier>("id", mapperEntryIdentifier));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, ChecklistListIdentifier>(LIST_ID, mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, UserIdentifier>(OWNER, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, Boolean>("completed", mapperBoolean));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ChecklistEntryBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
