package de.benjaminborbe.authentication.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.core.util.MapperUserIdentifier;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperByteArray;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.MapperTimeZone;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class UserBeanMapper extends MapObjectMapperAdapter<UserBean> {

	public static final String MODIFIED = "modified";

	public static final String CREATED = "created";

	public static final String LOGIN_COUNTER = "loginCounter";

	public static final String EMAIL_VERIFIED = "emailVerified";

	public static final String EMAIL_VERIFY_TOKEN = "emailVerifyToken";

	public static final String EMAIL = "email";

	public static final String EMAIL_NEW = "emailNew";

	public static final String TIME_ZONE = "timeZone";

	public static final String SUPER_ADMIN = "superAdmin";

	public static final String FULLNAME = "fullname";

	public static final String PASSWORD_SALT = "passwordSalt";

	public static final String PASSWORD = "password";

	public static final String ID = "id";

	public static final String LOGIN_DATE = "loginDate";

	@Inject
	public UserBeanMapper(
		final Provider<UserBean> provider,
		final MapperUserIdentifier mapperUserIdentifier,
		final MapperTimeZone mapperTimeZone,
		final MapperByteArray mapperByteArray,
		final MapperBoolean mapperBoolean,
		final MapperString mapperString,
		final MapperLong mapperLong,
		final MapperCalendar mapperCalendar) {
		super(provider, buildMappings(mapperUserIdentifier, mapperTimeZone, mapperByteArray, mapperBoolean, mapperString, mapperCalendar, mapperLong));
	}

	private static Collection<StringObjectMapper<UserBean>> buildMappings(final MapperUserIdentifier mapperUserIdentifier, final MapperTimeZone mapperTimeZone,
																																				final MapperByteArray mapperByteArray, final MapperBoolean mapperBoolean, final MapperString mapperString, final MapperCalendar mapperCalendar, final MapperLong mapperLong) {
		final List<StringObjectMapper<UserBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<UserBean, UserIdentifier>(ID, mapperUserIdentifier));
		result.add(new StringObjectMapperAdapter<UserBean, byte[]>(PASSWORD, mapperByteArray));
		result.add(new StringObjectMapperAdapter<UserBean, byte[]>(PASSWORD_SALT, mapperByteArray));
		result.add(new StringObjectMapperAdapter<UserBean, String>(FULLNAME, mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, Boolean>(SUPER_ADMIN, mapperBoolean));
		result.add(new StringObjectMapperAdapter<UserBean, TimeZone>(TIME_ZONE, mapperTimeZone));
		result.add(new StringObjectMapperAdapter<UserBean, String>(EMAIL, mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, String>(EMAIL_NEW, mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, String>(EMAIL_VERIFY_TOKEN, mapperString));
		result.add(new StringObjectMapperAdapter<UserBean, Boolean>(EMAIL_VERIFIED, mapperBoolean));
		result.add(new StringObjectMapperAdapter<UserBean, Long>(LOGIN_COUNTER, mapperLong));
		result.add(new StringObjectMapperAdapter<UserBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<UserBean, Calendar>(MODIFIED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<UserBean, Calendar>(LOGIN_DATE, mapperCalendar));
		return result;
	}
}
