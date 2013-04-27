package de.benjaminborbe.checklist.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.util.MapperListIdentifier;
import de.benjaminborbe.checklist.util.MapperUserIdentifier;
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
public class ChecklistListBeanMapper extends MapObjectMapperAdapter<ChecklistListBean> {

	public static final String OWNER = "owner";

	@Inject
	public ChecklistListBeanMapper(
		final Provider<ChecklistListBean> provider,
		final MapperListIdentifier mapperListIdentifier,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar

	) {
		super(provider, buildMappings(mapperListIdentifier, mapperUserIdentifier, mapperString, mapperCalendar));
	}

	private static Collection<StringObjectMapper<ChecklistListBean>> buildMappings(
		final MapperListIdentifier mapperListIdentifier, final MapperUserIdentifier mapperUserIdentifier,
		final MapperString mapperString, final MapperCalendar mapperCalendar
	) {
		final List<StringObjectMapper<ChecklistListBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<ChecklistListBean, ChecklistListIdentifier>("id", mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<ChecklistListBean, UserIdentifier>(OWNER, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<ChecklistListBean, String>("name", mapperString));
		result.add(new StringObjectMapperAdapter<ChecklistListBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<ChecklistListBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
