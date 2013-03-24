package de.benjaminborbe.notification.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.tools.registry.RegistryBase;

public class NotifcationNotifierRegistry extends RegistryBase<NotifcationNotifier> {

	private final Map<NotificationMediaIdentifier, NotifcationNotifier> data = new HashMap<NotificationMediaIdentifier, NotifcationNotifier>();

	@Inject
	public NotifcationNotifierRegistry(final NotifcationNotifierMail notifcationNotifierMail, final NotifcationNotifierXmpp notifcationNotifierXmpp) {
		add(notifcationNotifierXmpp);
		add(notifcationNotifierMail);
	}

	@Override
	protected void onElementAdded(final NotifcationNotifier object) {
		data.put(object.getNotificationMediaIdentifier(), object);
	}

	@Override
	protected void onElementRemoved(final NotifcationNotifier object) {
		data.remove(object.getNotificationMediaIdentifier());
	}

	public NotifcationNotifier get(final NotificationMediaIdentifier notificationMediaIdentifier) {
		return data.get(notificationMediaIdentifier);
	}

	public Collection<NotificationMediaIdentifier> getMedias() {
		return data.keySet();
	}
}
