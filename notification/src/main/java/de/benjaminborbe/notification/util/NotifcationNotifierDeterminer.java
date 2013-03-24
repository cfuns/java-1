package de.benjaminborbe.notification.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.dao.NotificationMediaDao;
import de.benjaminborbe.storage.api.StorageException;

public class NotifcationNotifierDeterminer {

	private final NotifcationNotifierRegistry notifcationNotifierRegistry;

	private final NotificationMediaDao notificationMediaDao;

	@Inject
	public NotifcationNotifierDeterminer(final NotifcationNotifierRegistry notifcationNotifierRegistry, final NotificationMediaDao notificationMediaDao) {
		this.notifcationNotifierRegistry = notifcationNotifierRegistry;
		this.notificationMediaDao = notificationMediaDao;
	}

	public Collection<NotifcationNotifier> getNotifcationNotifiers(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type) throws NotifcationNotifierException {
		try {
			final List<NotifcationNotifier> result = new ArrayList<NotifcationNotifier>();
			for (final NotificationMediaIdentifier notificationMediaIdentifier : notificationMediaDao.get(userIdentifier, type)) {
				final NotifcationNotifier n = notifcationNotifierRegistry.get(notificationMediaIdentifier);
				if (n != null) {
					result.add(n);
				}
			}
			return result;
		}
		catch (final StorageException e) {
			throw new NotifcationNotifierException(e);
		}
	}

}
