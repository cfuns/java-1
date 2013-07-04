package de.benjaminborbe.websearch.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

import java.net.URL;
import java.util.List;

public interface WebsearchConfiguration {

	WebsearchConfigurationIdentifier getId();

	URL getUrl();

	List<String> getExcludes();

	UserIdentifier getOwner();

	Integer getExpire();

	Long getDelay();

	Boolean getActivated();
}
