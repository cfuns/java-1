package de.benjaminborbe.notification.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.tools.registry.RegistryBase;

public class NotificationNotifierRegistry extends RegistryBase<NotificationNotifier> {

	private final Map<NotificationMediaIdentifier, NotificationNotifier> data = new HashMap<NotificationMediaIdentifier, NotificationNotifier>();

	@Inject
	public NotificationNotifierRegistry(final NotificationNotifierMail notifcationNotifierMail, final NotificationNotifierXmpp notifcationNotifierXmpp) {
		add(notifcationNotifierXmpp);
		add(notifcationNotifierMail);
	}

	@Override
	protected void onElementAdded(final NotificationNotifier object) {
		data.put(object.getNotificationMediaIdentifier(), object);
	}

	@Override
	protected void onElementRemoved(final NotificationNotifier object) {
		data.remove(object.getNotificationMediaIdentifier());
	}

	public NotificationNotifier get(final NotificationMediaIdentifier notificationMediaIdentifier) {
		return data.get(notificationMediaIdentifier);
	}

	public Collection<NotificationMediaIdentifier> getMedias() {
		return data.keySet();
	}
}
