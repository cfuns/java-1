package de.benjaminborbe.util.math;

public interface FormularParser {

	HasValue parse(String formular) throws FormularParseException;

}
