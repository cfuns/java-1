package de.benjaminborbe.monitoring.check;

import java.util.Collection;

public interface CheckRegistry {

	Collection<Check> getAll();

	void register(Check check);

}
