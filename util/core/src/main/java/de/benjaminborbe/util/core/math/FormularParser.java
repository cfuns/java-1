package de.benjaminborbe.util.core.math;

public interface FormularParser {

	HasValue parse(String formular) throws FormularParseException;

}
