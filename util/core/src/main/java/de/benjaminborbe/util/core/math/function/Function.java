package de.benjaminborbe.util.core.math.function;

import de.benjaminborbe.util.core.math.HasValue;

public interface Function {

	double calucate(HasValue... values);

	String asString(HasValue... values);
}
