package de.benjaminborbe.blog.dao;

import com.google.inject.Provider;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.util.MapperBlogPostIdentifier;
import de.benjaminborbe.blog.util.MapperUserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BlogPostBeanMapperUnitTest {

	private BlogPostBeanMapper getBlogPostBeanMapper() {
		final Provider<BlogPostBean> beanProvider = new ProviderMock<>(BlogPostBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperBlogPostIdentifier shortenerUrlIdentifierMapper = new MapperBlogPostIdentifier();
		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		return new BlogPostBeanMapper(beanProvider, shortenerUrlIdentifierMapper, mapperCalendar, mapperString, mapperUserIdentifier);
	}

	@Test
	public void testId() throws Exception {
		final BlogPostBeanMapper mapper = getBlogPostBeanMapper();
		final BlogPostIdentifier value = new BlogPostIdentifier("1337");
		final String fieldname = "id";
		{
			final BlogPostBean bean = new BlogPostBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<>();
			data.put(fieldname, String.valueOf(value));
			final BlogPostBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}
}
