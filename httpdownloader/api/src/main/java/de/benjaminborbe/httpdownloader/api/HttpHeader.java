package de.benjaminborbe.httpdownloader.api;

import java.util.Collection;
import java.util.List;

public interface HttpHeader {

	String getValue(String name);

	List<String> getValues(String name);

	Collection<String> getKeys();
}
