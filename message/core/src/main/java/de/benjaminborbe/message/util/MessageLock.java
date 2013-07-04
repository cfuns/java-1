package de.benjaminborbe.message.util;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class MessageLock {

	private final String lockName;

	public MessageLock() {
		this.lockName = String.valueOf(UUID.randomUUID());
	}

	public String getLockName() {
		return lockName;
	}
}
