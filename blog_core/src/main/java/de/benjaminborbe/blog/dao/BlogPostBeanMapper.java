package de.benjaminborbe.blog.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.util.MapperBlogPostIdentifier;
import de.benjaminborbe.blog.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class BlogPostBeanMapper extends MapObjectMapperAdapter<BlogPostBean> {

	@Inject
	public BlogPostBeanMapper(
			final Provider<BlogPostBean> provider,
			final MapperBlogPostIdentifier mapperBlogPostIdentifier,
			final MapperCalendar mapperCalendar,
			final MapperString mapperString,
			final MapperUserIdentifier mapperUserIdentifier) {
		super(provider, buildMappings(mapperUserIdentifier, mapperBlogPostIdentifier, mapperCalendar, mapperString));
	}

	private static Collection<StringObjectMapper<BlogPostBean>> buildMappings(final MapperUserIdentifier mapperUserIdentifier,
			final MapperBlogPostIdentifier mapperBlogPostIdentifier, final MapperCalendar mapperCalendar, final MapperString mapperString) {
		final List<StringObjectMapper<BlogPostBean>> result = new ArrayList<StringObjectMapper<BlogPostBean>>();
		result.add(new StringObjectMapperAdapter<BlogPostBean, BlogPostIdentifier>("id", mapperBlogPostIdentifier));
		result.add(new StringObjectMapperAdapter<BlogPostBean, UserIdentifier>("creator", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<BlogPostBean, String>("title", mapperString));
		result.add(new StringObjectMapperAdapter<BlogPostBean, String>("content", mapperString));
		result.add(new StringObjectMapperAdapter<BlogPostBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<BlogPostBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
