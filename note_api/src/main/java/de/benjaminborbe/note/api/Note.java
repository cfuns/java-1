package de.benjaminborbe.note.api;

import java.util.Calendar;

public interface Note {

	NoteIdentifier getId();

	String getTitle();

	String getContent();

	Calendar getCreated();

	Calendar getModified();

}
