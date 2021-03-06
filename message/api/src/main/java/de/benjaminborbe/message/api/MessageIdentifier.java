package de.benjaminborbe.message.api;

import de.benjaminborbe.api.IdentifierBase;

public class MessageIdentifier extends IdentifierBase<String> {

	public MessageIdentifier(final long id) {
		this(String.valueOf(id));
	}

	public MessageIdentifier(final String id) {
		super(id);
	}

}
