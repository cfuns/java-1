package de.benjaminborbe.wiki.dao;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class WikiPageBeanMapper extends MapObjectMapperAdapter<WikiPageBean> {

	@Inject
	public WikiPageBeanMapper(
		final Provider<WikiPageBean> provider,
		final MapperWikiPageIdentifier mapperWikiPageIdentifier,
		final MapperString mapperString,
		final MapperWikiSpaceIdentifier mapperWikiSpaceIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperWikiPageContentType mapperWikiPageContentType) {
		super(provider, buildMappings(mapperWikiPageIdentifier, mapperString, mapperWikiSpaceIdentifier, mapperCalendar, mapperWikiPageContentType));
	}

	private static Collection<StringObjectMapper<WikiPageBean>> buildMappings(final MapperWikiPageIdentifier mapperWikiPageIdentifier, final MapperString mapperString,
																																						final MapperWikiSpaceIdentifier mapperWikiSpaceIdentifier, final MapperCalendar mapperCalendar, final MapperWikiPageContentType mapperWikiPageContentType) {
		final List<StringObjectMapper<WikiPageBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiPageIdentifier>("id", mapperWikiPageIdentifier));
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiSpaceIdentifier>("space", mapperWikiSpaceIdentifier));
		result.add(new StringObjectMapperAdapter<WikiPageBean, String>("title", mapperString));
		result.add(new StringObjectMapperAdapter<WikiPageBean, String>("content", mapperString));
		result.add(new StringObjectMapperAdapter<WikiPageBean, WikiPageContentType>("contentType", mapperWikiPageContentType));
		result.add(new StringObjectMapperAdapter<WikiPageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WikiPageBean, Calendar>("modified", mapperCalendar));
		return result;
	}

}
