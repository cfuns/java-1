package de.benjaminborbe.filestorage.dao;

import com.google.inject.Provider;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.util.MapperFilestorageEntryIdentifier;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class FilestorageEntryBeanMapper extends MapObjectMapperAdapter<FilestorageEntryBean> {

	public static final String ID = "id";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String FILENAME = "filename";

	public static final String CONTENT_TYPE = "contentType";

	public static final String CONTENT = "content";

	@Inject
	public FilestorageEntryBeanMapper(
		final Provider<FilestorageEntryBean> provider,
		final MapperCalendar mapperCalendar,
		final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier,
		final MapperString mapperString,
		final MapperByteArray mapperByteArray
	) {
		super(provider, buildMappings(mapperCalendar, mapperFilestorageEntryIdentifier, mapperString, mapperByteArray));
	}

	private static Collection<StringObjectMapper<FilestorageEntryBean>> buildMappings(
		final MapperCalendar mapperCalendar,
		final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier,
		final MapperString mapperString,
		final MapperByteArray mapperByteArray
	) {
		final List<StringObjectMapper<FilestorageEntryBean>> result = new ArrayList<StringObjectMapper<FilestorageEntryBean>>();
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, FilestorageEntryIdentifier>(ID, mapperFilestorageEntryIdentifier));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, String>(FILENAME, mapperString));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, String>(CONTENT_TYPE, mapperString));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, byte[]>(CONTENT, mapperByteArray));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<FilestorageEntryBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
