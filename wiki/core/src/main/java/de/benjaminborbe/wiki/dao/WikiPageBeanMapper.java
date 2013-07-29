package de.benjaminborbe.wiki.dao;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.wiki.api.WikiPageContentType;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.util.MapperWikiPageContentType;
import de.benjaminborbe.wiki.util.MapperWikiPageIdentifier;
import de.benjaminborbe.wiki.util.MapperWikiSpaceIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class WikiPageBeanMapper extends MapObjectMapperAdapter<WikiPageBean> {

	public static final String ID = "id";

	public static final String SPACE = "space";

	public static final String TITLE = "title";

	public static final String CONTENT = "content";

	public static final String CONTENT_TYPE = "contentType";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	@Inject
	public WikiPageBeanMapper(
		final Provider<WikiPageBean> provider,
		final MapperWikiPageIdentifier mapperWikiPageIdentifier,
		final MapperString mapperString,
		final MapperWikiSpaceIdentifier mapperWikiSpaceIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperWikiPageContentType mapperWikiPageContentType
	) {
		super(provider, buildMappings(mapperWikiPageIdentifier, mapperString, mapperWikiSpaceIdentifier, mapperCalendar, mapperWikiPageContentType));
	}

	private static Collection<StringObjectMapper<WikiPageBean>> buildMappings(
		final MapperWikiPageIdentifier mapperWikiPageIdentifier, final MapperString mapperString,
		final MapperWikiSpaceIdentifier mapperWikiSpaceIdentifier, final MapperCalendar mapperCalendar, final MapperWikiPageContentType mapperWikiPageContentType
	) {
		final List<StringObjectMapper<WikiPageBean>> result = new ArrayList<StringObjectMapper<WikiPageBean>>();
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiPageIdentifier>(ID, mapperWikiPageIdentifier));
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiSpaceIdentifier>(SPACE, mapperWikiSpaceIdentifier));
		result.add(new StringObjectMapperAdapter<WikiPageBean, String>(TITLE, mapperString));
		result.add(new StringObjectMapperAdapter<WikiPageBean, String>(CONTENT, mapperString));
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiPageContentType>(CONTENT_TYPE, mapperWikiPageContentType));
		result.add(new StringObjectMapperAdapter<WikiPageBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<WikiPageBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}

}
