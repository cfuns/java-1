package de.benjaminborbe.monitoring.check;

import java.util.Collection;

public interface CheckNode {

	Check getCheck();

	Collection<CheckNode> getChildNodes();

	Collection<CheckResult> check();
}
