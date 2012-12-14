package de.benjaminborbe.confluence.api;

public interface ConfluenceInstance {

	ConfluenceInstanceIdentifier getId();

	String getUrl();

	String getUsername();

	Integer getExpire();

	Boolean getShared();

	Long getDelay();

	Boolean getActivated();

}
