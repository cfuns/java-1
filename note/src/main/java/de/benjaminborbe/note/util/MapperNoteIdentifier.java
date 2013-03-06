package de.benjaminborbe.note.util;

import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperNoteIdentifier implements Mapper<NoteIdentifier> {

	@Override
	public String toString(final NoteIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public NoteIdentifier fromString(final String value) {
		return value != null ? new NoteIdentifier(value) : null;
	}

}
