package de.benjaminborbe.monitoring.check;

import java.util.Collection;

public interface HasChildNodes extends Node {

	Collection<Node> getChildNodes();
}
