package de.benjaminborbe.filestorage.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.util.MapperFilestorageEntryIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class FilestorageEntryBeanMapper extends MapObjectMapperAdapter<FilestorageEntryBean> {

	@Inject
	public FilestorageEntryBeanMapper(
		final Provider<FilestorageEntryBean> provider,
		final MapperCalendar mapperCalendar, final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier) {
		super(provider, buildMappings(mapperCalendar, mapperFilestorageEntryIdentifier));
	}

	private static Collection<StringObjectMapper<FilestorageEntryBean>> buildMappings(final MapperCalendar mapperCalendar, final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier) {
		final List<StringObjectMapper<FilestorageEntryBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, FilestorageEntryIdentifier>("id", mapperFilestorageEntryIdentifier));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
