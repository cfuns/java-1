package de.benjaminborbe.util.math.operation;

import de.benjaminborbe.util.math.HasValue;

public interface Operation {

	double calucate(HasValue valueA, HasValue valueB);

	String asString(HasValue valueA, HasValue valueB);
}
