package de.benjaminborbe.mail.util;

import javax.inject.Inject;
import com.google.inject.Provider;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.json.JsonObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MailJsonMapper {

	private final JsonObjectMapper<MailDto> mapper;

	@Inject
	public MailJsonMapper(final Provider<MailDto> provider, final MapperString mapperString) {
		mapper = new JsonObjectMapper<>(provider, build(mapperString));
	}

	private Collection<StringObjectMapper<MailDto>> build(final MapperString mapperString) {
		final List<StringObjectMapper<MailDto>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<MailDto, String>("from", mapperString));
		result.add(new StringObjectMapperAdapter<MailDto, String>("to", mapperString));
		result.add(new StringObjectMapperAdapter<MailDto, String>("subject", mapperString));
		result.add(new StringObjectMapperAdapter<MailDto, String>("content", mapperString));
		result.add(new StringObjectMapperAdapter<MailDto, String>("contentType", mapperString));
		return result;
	}

	public String map(final Mail mail) throws MapException {
		return map(new MailDto(mail));
	}

	public String map(final MailDto mail) throws MapException {
		return mapper.toJson(mail);
	}

	public MailDto map(final String message) throws MapException {
		return mapper.fromJson(message);
	}

}
