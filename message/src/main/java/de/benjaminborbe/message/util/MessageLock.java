package de.benjaminborbe.message.util;

import java.util.UUID;

import com.google.inject.Singleton;

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
