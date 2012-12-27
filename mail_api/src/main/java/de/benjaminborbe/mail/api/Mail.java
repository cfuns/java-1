package de.benjaminborbe.mail.api;

public interface Mail {

	String getFrom();

	String getTo();

	String getSubject();

	String getContent();

	String getContentType();

}
