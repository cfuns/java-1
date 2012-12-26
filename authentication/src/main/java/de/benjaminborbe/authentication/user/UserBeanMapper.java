package de.benjaminborbe.authentication.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.MapperTimeZone;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

@Singleton
public class UserBeanMapper extends MapObjectMapperAdapter<UserBean> {

	@Inject
	public UserBeanMapper(
			final Provider<UserBean> provider,
			final MapperUserIdentifier mapperUserIdentifier,
			final MapperTimeZone mapperTimeZone,
			final MapperByteArray mapperByteArray,
			final MapperBoolean mapperBoolean,
			final MapperString mapperString) {
		super(provider, buildMappings(mapperUserIdentifier, mapperTimeZone, mapperByteArray, mapperBoolean, mapperString));
	}

	private static Collection<StringObjectMapper<UserBean>> buildMappings(final MapperUserIdentifier mapperUserIdentifier, final MapperTimeZone mapperTimeZone,
			final MapperByteArray mapperByteArray, final MapperBoolean mapperBoolean, final MapperString mapperString) {
		final List<StringObjectMapper<UserBean>> result = new ArrayList<StringObjectMapper<UserBean>>();
		result.add(new StringObjectMapperAdapter<UserBean, UserIdentifier>("id", mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<UserBean, byte[]>("password", mapperByteArray));
		result.add(new StringObjectMapperAdapter<UserBean, byte[]>("passwordSalt", mapperByteArray));
		result.add(new StringObjectMapperAdapter<UserBean, String>("fullname", mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, String>("email", mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, Boolean>("superAdmin", mapperBoolean));
		result.add(new StringObjectMapperAdapter<UserBean, TimeZone>("timeZone", mapperTimeZone));
		return result;
	}
}
