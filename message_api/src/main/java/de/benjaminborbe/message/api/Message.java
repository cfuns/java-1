package de.benjaminborbe.message.api;

import java.util.Calendar;

public interface Message {

	MessageIdentifier getId();

	String getType();

	String getContent();

	Long getRetryCounter();

	String getLockName();

	Calendar getLockTime();

	Long getMaxRetryCounter();

	Calendar getStartTime();

	Calendar getCreated();

	Calendar getModified();
}
