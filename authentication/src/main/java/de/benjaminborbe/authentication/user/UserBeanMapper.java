package de.benjaminborbe.authentication.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperBoolean;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperByteArray;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperString;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperTimeZone;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class UserBeanMapper extends MapObjectMapperAdapter<UserBean> {

	@Inject
	public UserBeanMapper(final Provider<UserBean> provider, final ParseUtil parseUtil, final CalendarUtil calendarUtil, final Base64Util base64Util) {
		super(provider, buildMappings(parseUtil, calendarUtil, base64Util));
	}

	private static Collection<StringObjectMapper<UserBean>> buildMappings(final ParseUtil parseUtil, final CalendarUtil calendarUtil, final Base64Util base64Util) {
		final List<StringObjectMapper<UserBean>> result = new ArrayList<StringObjectMapper<UserBean>>();
		result.add(new StringObjectMapperUserIdentifier<UserBean>("id"));
		result.add(new StringObjectMapperByteArray<UserBean>("password", base64Util));
		result.add(new StringObjectMapperByteArray<UserBean>("passwordSalt", base64Util));
		result.add(new StringObjectMapperString<UserBean>("fullname"));
		result.add(new StringObjectMapperString<UserBean>("email"));
		result.add(new StringObjectMapperBoolean<UserBean>("superAdmin", parseUtil));
		result.add(new StringObjectMapperTimeZone<UserBean>("timeZone"));
		return result;
	}

}
