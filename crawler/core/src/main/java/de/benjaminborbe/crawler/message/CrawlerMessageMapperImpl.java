package de.benjaminborbe.crawler.message;

import javax.inject.Inject;
import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrawlerMessageMapperImpl implements CrawlerMessageMapper {

	private final JsonObjectMapper<CrawlerMessage> mapper;

	@Inject
	public CrawlerMessageMapperImpl(final Provider<CrawlerMessage> messageProvider, final MapperUrl mapperUrl, final MapperInteger mapperInteger) {
		mapper = new JsonObjectMapper<>(messageProvider, build(mapperUrl, mapperInteger));
	}

	private Collection<StringObjectMapper<CrawlerMessage>> build(final MapperUrl mapperUrl, final MapperInteger mapperInteger) {
		final List<StringObjectMapper<CrawlerMessage>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<CrawlerMessage, URL>("url", mapperUrl));
		result.add(new StringObjectMapperAdapter<CrawlerMessage, Integer>("timeout", mapperInteger));
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
