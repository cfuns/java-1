package de.benjaminborbe.cron.message;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CronMessageMapperImpl implements CronMessageMapper {

	private final JsonObjectMapper<CronMessage> mapper;

	@Inject
	public CronMessageMapperImpl(final Provider<CronMessage> messageProvider, final MapperString mapperString) {
		mapper = new JsonObjectMapper<>(messageProvider, build(mapperString));
	}

	private Collection<StringObjectMapper<CronMessage>> build(final MapperString mapperString) {
		final List<StringObjectMapper<CronMessage>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<CronMessage, String>("name", mapperString));
		return result;
	}

	@Override
	public String map(final CronMessage message) throws MapException {
		return mapper.toJson(message);
	}

	@Override
	public CronMessage map(final String message) throws MapException {
		return mapper.fromJson(message);
	}
}
