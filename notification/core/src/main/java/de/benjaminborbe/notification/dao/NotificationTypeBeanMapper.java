package de.benjaminborbe.notification.dao;

import com.google.inject.Provider;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.util.MapperNotificationTypeIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class NotificationTypeBeanMapper extends MapObjectMapperAdapter<NotificationTypeBean> {

	public static final String OWNER = "owner";

	@Inject
	public NotificationTypeBeanMapper(
		final Provider<NotificationTypeBean> provider,
		final MapperNotificationTypeIdentifier mapperNotificationTypeIdentifier,
		final MapperCalendar mapperCalendar
	) {
		super(provider, buildMappings(mapperCalendar, mapperNotificationTypeIdentifier));
	}

	private static Collection<StringObjectMapper<NotificationTypeBean>> buildMappings(
		final MapperCalendar mapperCalendar,
		final MapperNotificationTypeIdentifier mapperNotificationTypeIdentifier
	) {
		final List<StringObjectMapper<NotificationTypeBean>> result = new ArrayList<StringObjectMapper<NotificationTypeBean>>();
		result.add(new StringObjectMapperAdapter<NotificationTypeBean, NotificationTypeIdentifier>("id", mapperNotificationTypeIdentifier));
		result.add(new StringObjectMapperAdapter<NotificationTypeBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<NotificationTypeBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
