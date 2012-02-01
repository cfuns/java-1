package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.Check;

public interface HasCheckNode extends Node {

	Check getCheck();
}
