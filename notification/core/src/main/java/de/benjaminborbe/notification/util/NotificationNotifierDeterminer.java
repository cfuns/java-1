package de.benjaminborbe.notification.util;

import javax.inject.Inject;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.storage.api.StorageException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NotificationNotifierDeterminer {

	private final NotificationNotifierRegistry notifcationNotifierRegistry;

	private final NotificationMediaDao notificationMediaDao;

	@Inject
	public NotificationNotifierDeterminer(final NotificationNotifierRegistry notifcationNotifierRegistry, final NotificationMediaDao notificationMediaDao) {
		this.notifcationNotifierRegistry = notifcationNotifierRegistry;
		this.notificationMediaDao = notificationMediaDao;
	}

	public Collection<NotificationNotifier> getNotifcationNotifiers(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type) throws NotificationNotifierException {
		try {
			final List<NotificationNotifier> result = new ArrayList<>();
			for (final NotificationMediaIdentifier notificationMediaIdentifier : notificationMediaDao.get(userIdentifier, type)) {
				final NotificationNotifier n = notifcationNotifierRegistry.get(notificationMediaIdentifier);
				if (n != null) {
					result.add(n);
				}
			}
			return result;
		} catch (final StorageException e) {
			throw new NotificationNotifierException(e);
		}
	}

}
