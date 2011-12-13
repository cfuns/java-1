package de.benjaminborbe.monitoring.check;

import java.util.Collection;

public interface CheckParent extends Check {

	Collection<Check> getChildChecks();
}
