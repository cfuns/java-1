package de.benjaminborbe.crawler.message;

import com.google.inject.Provider;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class CrawlerMessageMapperImplUnitTest {

	@Test
	public void testMapDepth() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final Provider<CrawlerMessage> messageProvider = new ProviderMock<CrawlerMessage>(CrawlerMessage.class);
		final MapperUrl mapperUrl = new MapperUrl(parseUtil);
		final MapperInteger mapperInteger = new MapperInteger(parseUtil);
		final MapperLong mapperLong = new MapperLong(parseUtil);
		final CrawlerMessageMapperImpl crawlerMessageMapper = new CrawlerMessageMapperImpl(messageProvider, mapperUrl, mapperInteger, mapperLong);

		final URL url = new URL("http://www.benjamin-borbe.de");
		final long depth = 1337l;
		final int timeout = 42;
		final CrawlerMessage orgMessage = new CrawlerMessage(url, depth, timeout);
		final String string = crawlerMessageMapper.map(orgMessage);
		final CrawlerMessage message = crawlerMessageMapper.map(string);
		assertThat(message, is(notNullValue()));
		assertThat(message.getTimeout(), is(timeout));
		assertThat(message.getUrl(), is(url));
		assertThat(message.getDepth(), is(depth));
	}

}
