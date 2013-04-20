package de.benjaminborbe.lunch.util;

import javax.inject.Inject;

import de.benjaminborbe.tools.registry.RegistryBase;

public class LunchUserNotifierRegistry extends RegistryBase<LunchUserNotifier> {

	@Inject
	public LunchUserNotifierRegistry(final LunchUserNotifierNotification lunchUserNotifierNotification) {
		super(lunchUserNotifierNotification);
	}

}
