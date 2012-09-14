package de.benjaminborbe.util.math.operation;

import java.util.HashMap;
import java.util.Map;

import de.benjaminborbe.util.math.HasValue;

public class Operations {

	private final Map<String, Operation> operations = new HashMap<String, Operation>();

	public Operations() {
		operations.put("+", new Addition());
		operations.put("-", new Subtraction());
		operations.put("/", new Division());
		operations.put("*", new Multiply());
	}

	public boolean exists(final String operationIdentifier) {
		return operations.containsKey(operationIdentifier);
	}

	public HasValue get(final String operationIdentifier, final HasValue valueA, final HasValue valueB) {
		return new OperationValue(operations.get(operationIdentifier), valueA, valueB);
	}
}
