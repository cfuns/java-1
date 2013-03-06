package de.benjaminborbe.note.api;

import java.util.Calendar;

public class NoteDto implements Note {

	private NoteIdentifier id;

	private String title;

	private String content;

	private Calendar created;

	private Calendar modified;

	@Override
	public NoteIdentifier getId() {
		return id;
	}

	public void setId(final NoteIdentifier id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}
}
