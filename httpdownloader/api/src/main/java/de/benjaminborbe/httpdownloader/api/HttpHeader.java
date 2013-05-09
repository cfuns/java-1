package de.benjaminborbe.httpdownloader.api;

import java.util.Collection;
import java.util.List;

public interface HttpHeader {

	String getValue(String key);

	List<String> getValues(String key);

	Collection<String> getKeys();
}
