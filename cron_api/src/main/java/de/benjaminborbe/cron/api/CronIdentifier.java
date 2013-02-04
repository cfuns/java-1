package de.benjaminborbe.cron.api;

import de.benjaminborbe.api.IdentifierBase;

public class CronIdentifier extends IdentifierBase<String> {

	public CronIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public CronIdentifier(final String id) {
		super(id);
	}

}
