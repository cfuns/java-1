package de.benjaminborbe.websearch.configuration;

import java.net.URL;
import java.util.List;

public interface Configuration {

	URL getUrl();

	String getOwnerUsername();

	List<String> getExcludes();
}
