package de.benjaminborbe.note.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.note.api.NoteIdentifier;

public class NoteIdentifierBuilder implements IdentifierBuilder<String, NoteIdentifier> {

	@Override
	public NoteIdentifier buildIdentifier(final String value) {
		return new NoteIdentifier(value);
	}

}
