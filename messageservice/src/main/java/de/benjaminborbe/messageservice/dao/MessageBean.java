package de.benjaminborbe.messageservice.dao;

import java.util.Calendar;

import de.benjaminborbe.messageservice.api.Message;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class MessageBean implements Entity<MessageIdentifier>, Message, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private MessageIdentifier id;

	private String content;

	private Calendar created;

	private Calendar modified;

	private String type;

	private Long retryCounter;

	@Override
	public MessageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final MessageIdentifier id) {
		this.id = id;
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

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Long getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(Long retryCounter) {
		this.retryCounter = retryCounter;
	}

}
