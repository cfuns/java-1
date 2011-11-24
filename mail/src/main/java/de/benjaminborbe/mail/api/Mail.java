package de.benjaminborbe.mail.api;

import java.io.Serializable;

public class Mail implements Serializable {

	private static final long serialVersionUID = 4356900317360469529L;

	private final String from;

	private final String to;

	private final String subject;

	private final String content;

	public Mail(final String from, final String to, final String subject, final String content) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getSubject() {
		return subject;
	}

	public String getContent() {
		return content;
	}

}
