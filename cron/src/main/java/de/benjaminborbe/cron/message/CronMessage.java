package de.benjaminborbe.cron.message;

public class CronMessage {

	private String name;

	public CronMessage() {
	}

	public CronMessage(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
