package de.benjaminborbe.confluence.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface ConfluenceInstance {

	ConfluenceInstanceIdentifier getId();

	String getUrl();

	String getUsername();

	Integer getExpire();

	Boolean getShared();

	Long getDelay();

	Boolean getActivated();

	UserIdentifier getOwner();

}
