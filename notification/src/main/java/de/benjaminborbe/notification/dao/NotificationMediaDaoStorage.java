package de.benjaminborbe.notification.dao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.api.NotificationTypeIdentifier;
import de.benjaminborbe.notification.util.NotifcationNotifierRegistry;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;

@Singleton
public class NotificationMediaDaoStorage implements NotificationMediaDao {

	private static final String TRUE = Boolean.TRUE.toString();

	private static final String COLUMN_FAMILY = "notification_media";

	private final StorageService storageService;

	private final NotifcationNotifierRegistry notifcationNotifierRegistry;

	private final Logger logger;

	@Inject
	public NotificationMediaDaoStorage(final Logger logger, final StorageService storageService, final NotifcationNotifierRegistry notifcationNotifierRegistry) {
		this.logger = logger;
		this.storageService = storageService;
		this.notifcationNotifierRegistry = notifcationNotifierRegistry;
	}

	@Override
	public void add(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type, final NotificationMediaIdentifier notificationMediaIdentifier)
			throws StorageException {
		storageService.set(COLUMN_FAMILY, getKey(userIdentifier, type), getValue(notificationMediaIdentifier.getId()), getValue(TRUE));
	}

	@Override
	public void remove(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type, final NotificationMediaIdentifier notificationMediaIdentifier)
			throws StorageException {
		storageService.delete(COLUMN_FAMILY, getKey(userIdentifier, type), getValue(notificationMediaIdentifier.getId()));
	}

	@Override
	public Collection<NotificationMediaIdentifier> get(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type) throws StorageException {
		try {
			logger.debug("get - user: " + userIdentifier + " type: " + type);
			final List<NotificationMediaIdentifier> result = new ArrayList<NotificationMediaIdentifier>();
			final Map<StorageValue, StorageValue> data = storageService.get(COLUMN_FAMILY, getKey(userIdentifier, type));
			for (final NotificationMediaIdentifier notificationMediaIdentifier : notifcationNotifierRegistry.getMedias()) {
				final StorageValue v = data.get(getValue(notificationMediaIdentifier.getId()));
				if (v != null && TRUE.equals(v.getString())) {
					result.add(notificationMediaIdentifier);
				}
			}
			return result;
		}
		catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

	private StorageValue getValue(final String value) {
		return new StorageValue(value, storageService.getEncoding());
	}

	private StorageValue getKey(final UserIdentifier userIdentifier, final NotificationTypeIdentifier type) {
		return new StorageValue(userIdentifier + "-" + type, storageService.getEncoding());
	}

	@Override
	public boolean has(final UserIdentifier userIdentifier, final NotificationTypeIdentifier notificationTypeIdentifier, final NotificationMediaIdentifier notificationMediaIdentifier)
			throws StorageException {
		try {
			final StorageValue v = storageService.get(COLUMN_FAMILY, getKey(userIdentifier, notificationTypeIdentifier), getValue(notificationMediaIdentifier.getId()));
			return TRUE.equals(v.getString());
		}
		catch (final UnsupportedEncodingException e) {
			throw new StorageException(e);
		}
	}

}
