package de.benjaminborbe.note.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.note.api.Note;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class NoteBean extends EntityBase<NoteIdentifier> implements Note, HasCreated, HasModified {

	private static final long serialVersionUID = 3767610391938515637L;

	private NoteIdentifier id;

	private String title;

	private String content;

	private Calendar created;

	private Calendar modified;

	private UserIdentifier owner;

	@Override
	public NoteIdentifier getId() {
		return id;
	}

	@Override
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

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

}
