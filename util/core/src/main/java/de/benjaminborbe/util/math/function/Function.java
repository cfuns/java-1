package de.benjaminborbe.util.math.function;

import de.benjaminborbe.util.math.HasValue;

public interface Function {

	double calucate(HasValue... values);

	String asString(HasValue... values);
}
