package de.benjaminborbe.lunch.util;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;

public class LunchUserNotifierRegistry extends RegistryBase<LunchUserNotifier> {

	@Inject
	public LunchUserNotifierRegistry(final LunchUserNotifierNotification lunchUserNotifierNotification) {
		super(lunchUserNotifierNotification);
	}

}
