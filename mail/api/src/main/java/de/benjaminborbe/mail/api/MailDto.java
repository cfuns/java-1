package de.benjaminborbe.mail.api;

import java.io.Serializable;

public class MailDto implements Mail, Serializable {

	private static final long serialVersionUID = 4356900317360469529L;

	private String from;

	private String to;

	private String subject;

	private String content;

	private String contentType;

	public MailDto() {
	}

	public MailDto(final Mail mail) {
		this.from = mail.getFrom();
		this.to = mail.getTo();
		this.subject = mail.getSubject();
		this.content = mail.getContent();
		this.contentType = mail.getContentType();
	}

	public MailDto(final String from, final String to, final String subject, final String content, final String contentType) {
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.content = content;
		this.contentType = contentType;
	}

	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public String getTo() {
		return to;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setFrom(final String from) {
		this.from = from;
	}

	public void setTo(final String to) {
		this.to = to;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

}
