package de.benjaminborbe.authentication.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.SingleMap;
import de.benjaminborbe.tools.mapper.SingleMapBoolean;
import de.benjaminborbe.tools.mapper.SingleMapByteArray;
import de.benjaminborbe.tools.mapper.SingleMapString;
import de.benjaminborbe.tools.mapper.SingleMapTimeZone;
import de.benjaminborbe.tools.mapper.SingleMappler;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class UserBeanMapper extends SingleMappler<UserBean> {

	@Inject
	public UserBeanMapper(final Provider<UserBean> provider, final ParseUtil parseUtil, final CalendarUtil calendarUtil, final Base64Util base64Util) {
		super(provider, buildMappings(parseUtil, calendarUtil, base64Util));
	}

	private static Collection<SingleMap<UserBean>> buildMappings(final ParseUtil parseUtil, final CalendarUtil calendarUtil, final Base64Util base64Util) {
		final List<SingleMap<UserBean>> result = new ArrayList<SingleMap<UserBean>>();
		result.add(new SingleMapUserIdentifier<UserBean>("id"));
		result.add(new SingleMapByteArray<UserBean>("password", base64Util));
		result.add(new SingleMapByteArray<UserBean>("passwordSalt", base64Util));
		result.add(new SingleMapString<UserBean>("fullname"));
		result.add(new SingleMapString<UserBean>("email"));
		result.add(new SingleMapBoolean<UserBean>("superAdmin", parseUtil));
		result.add(new SingleMapTimeZone<UserBean>("timeZone"));
		return result;
	}

}
