package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.HasValue;

public interface Operation {

	double calucate(HasValue valueA, HasValue valueB);

	String asString(HasValue valueA, HasValue valueB);
}
