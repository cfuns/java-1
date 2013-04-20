package de.benjaminborbe.notification.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class NotificationTypeDaoStorage extends DaoStorage<NotificationTypeBean, NotificationTypeIdentifier> implements NotificationTypeDao {

	private static final String COLUMN_FAMILY = "notification_type";

	@Inject
	public NotificationTypeDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<NotificationTypeBean> beanProvider,
			final NotificationTypeBeanMapper mapper,
			final NotificationTypeIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
