package de.benjaminborbe.crawler.message;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrawlerMessageMapperImpl implements CrawlerMessageMapper {

	public static final String TIMEOUT = "timeout";

	public static final String DEPTH = "depth";

	public static final String URL = "url";

	private final JsonObjectMapper<CrawlerMessage> mapper;

	@Inject
	public CrawlerMessageMapperImpl(
		final Provider<CrawlerMessage> messageProvider,
		final MapperUrl mapperUrl,
		final MapperInteger mapperInteger,
		final MapperLong mapperLong
	) {
		mapper = new JsonObjectMapper<CrawlerMessage>(messageProvider, build(mapperUrl, mapperInteger, mapperLong));
	}

	private Collection<StringObjectMapper<CrawlerMessage>> build(final MapperUrl mapperUrl, final MapperInteger mapperInteger, final MapperLong mapperLong) {
		final List<StringObjectMapper<CrawlerMessage>> result = new ArrayList<StringObjectMapper<CrawlerMessage>>();
		result.add(new StringObjectMapperAdapter<CrawlerMessage, URL>(URL, mapperUrl));
		result.add(new StringObjectMapperAdapter<CrawlerMessage, Integer>(TIMEOUT, mapperInteger));
		result.add(new StringObjectMapperAdapter<CrawlerMessage, Long>(DEPTH, mapperLong));
		return result;
	}

	@Override
	public String map(final CrawlerMessage message) throws MapException {
		return mapper.toJson(message);
	}

	@Override
	public CrawlerMessage map(final String message) throws MapException {
		return mapper.fromJson(message);
	}
}
