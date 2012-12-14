package de.benjaminborbe.websearch.api;

import java.net.URL;
import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface WebsearchConfiguration {

	WebsearchConfigurationIdentifier getId();

	URL getUrl();

	List<String> getExcludes();

	UserIdentifier getOwner();

	Integer getExpire();

	Long getDelay();
}
